package bg.tsarstva.follow.api.security;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.core.PropertyReader;
import bg.tsarstva.follow.api.database.query.GetUserQuery;
import bg.tsarstva.follow.api.entity.User;

/**
 * Manage user login lockouts
 * @author ivaylo.marinkov
 *
 */

public class UserLockoutManager {
	
	private static final String SELECT_LOCKOUT_STATEMENT  = "SELECT * FROM cfollow.`cf_security.user.lockout` WHERE userid = ?;";
	private static final String ADD_LOCKOUT_STATEMENT     = "INSERT INTO `cfollow`.`cf_security.user.lockout` (`userid`, `failcount`, `lastfailure`) VALUES (?, '1', ?) ON DUPLICATE KEY UPDATE failcount = failcount+1, lastfailure = ?;";
	private static final String DELETE_STATEMENT		  = "DELETE FROM cfollow.`cf_security.user.lockout` WHERE userid = ?;";
	private static final int LOCKOUT_COOLDOWN_MULTIPLIER  = Integer.parseInt(PropertyReader.getInstance().getProperty("security.user.timeout.multiplier"));
	private static final int LOCKOUT_UNPENALIZED_ATTEMPTS = Integer.parseInt(PropertyReader.getInstance().getProperty("security.user.lock.unpenalized.failed.attempts"));
	private static final int LOCKOUT_MAX_PERIOD 		  = Integer.parseInt(PropertyReader.getInstance().getProperty("security.lockout.max"));
	
	private static final Logger LOGGER = Logger.getLogger(UserLockoutManager.class.getName());
	
	private String username;
	private String email;
	private int userId;
	private boolean useUsername;
	private Timestamp lastFailureDate;
	private int failCount;
	
	// The cooldown period is the amount of time that must have passed
	// before a user can be removed from the lockout table.
	// By design it is equal to the number of times login has failed (failcount) times 5, in minutes
	// The multiplier is however configurable
	private long cooldownPeriod;
	
	public UserLockoutManager() {
		
	}
	
	public void setUsername(String username) {
		this.username = username;
		useUsername = true;
	}
	
	public void setEmail(String email) {
		this.email = email;
		useUsername = false;
	}
	
	public synchronized boolean isLocked() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(SELECT_LOCKOUT_STATEMENT);
		String usernameOrEmail              = useUsername ? username : email;
		User user = new GetUserQuery(usernameOrEmail).execute().getResult();
		ResultSet results;
		long lockoutThresholdTime; 
		
		// A non-existent user might as well be always locked
		if(!user.getIsValid()) {
			LOGGER.info("Requested user " + usernameOrEmail + " was not found to exist. Returning locked.");
			return true;
		}
		
		userId = user.getUserId();
		statement.setInt(1, userId);
		
		results = statement.executeQuery();
		
		// The user is not locked - No such entry exists in the table
		if(!results.first()) {
			LOGGER.info("User " + usernameOrEmail + " not present in user lockout table. Returning unlocked.");
			return false;
		}
		
		lastFailureDate = results.getTimestamp("lastfailure");
		failCount = results.getInt("failcount");
		cooldownPeriod = getCooldownPeriod(failCount);
		
		// The time stamp when the user will no longer be locked
		lockoutThresholdTime = lastFailureDate.getTime() + cooldownPeriod;
		
		if(failCount > LOCKOUT_UNPENALIZED_ATTEMPTS && System.currentTimeMillis() < lockoutThresholdTime) {
			logLockedStatus(usernameOrEmail, failCount, lockoutThresholdTime, System.currentTimeMillis());
			return true;
		} else {
			return false;
		}
	}
	
	private static long getCooldownPeriod(int failCount) {
		long cooldown = failCount * LOCKOUT_COOLDOWN_MULTIPLIER;
		long maxPeriod = LOCKOUT_MAX_PERIOD * 60000;
		
		if(cooldown >= maxPeriod) {
			return maxPeriod;
		} else {
			return cooldown;
		}
	}
	
	public synchronized static void addLock(int userId) throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(ADD_LOCKOUT_STATEMENT);
		Date date 							= new Date(new java.util.Date().getTime());
		Timestamp timestamp 				= new Timestamp(date.getTime());
		
		statement.setInt(1, userId);
		statement.setTimestamp(2, timestamp);
		statement.setTimestamp(3, timestamp);
		
		statement.executeUpdate();
		LOGGER.info("Added user lock for user " + userId);
	}
	
	public synchronized static void deleteLock(int userId) throws SQLException, ClassNotFoundException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(DELETE_STATEMENT);
		
		statement.setInt(1, userId);
		
		if(statement.executeUpdate() == 1) {
			LOGGER.info("Deleted lock for user " + userId);
		}
	}
	
	private void logLockedStatus(String usernameOrEmail, int failCount, long lockoutThresholdTime, long currentTime) {
		String message = "User %s is locked: Current fail count is %d (unpenalized=%d). User will be unlocked after %d. It is now %d.";
		
		message = String.format(message, usernameOrEmail, failCount, LOCKOUT_UNPENALIZED_ATTEMPTS, lockoutThresholdTime, currentTime);
		LOGGER.info(message);
	}
}
