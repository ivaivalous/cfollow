package bg.tsarstva.follow.api.security;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.core.PropertyReader;

/**
 * Manage IP lockouts over login
 * @author ivaylo.marinkov
 *
 */

public class IpLockoutManager {
	
	private static final String SELECT_LOCKOUT_STATEMENT  = "SELECT * FROM cfollow.`cf_security.ip.lockout` WHERE ip = ?;";
	private static final String ADD_LOCKOUT_STATEMENT     = "INSERT INTO `cfollow`.`cf_security.ip.lockout` (`ip`, `failcount`, `lastfailure`) VALUES (?, '1', ?) ON DUPLICATE KEY UPDATE failcount = failcount+1, lastfailure = ?;";
	private static final String DELETE_STATEMENT		  = "DELETE FROM cfollow.`cf_security.ip.lockout` WHERE ip = ?;";
	private static final int LOCKOUT_COOLDOWN_MULTIPLIER  = Integer.parseInt(PropertyReader.getInstance().getProperty("security.ip.timeout.multiplier"));
	private static final int LOCKOUT_UNPENALIZED_ATTEMPTS = Integer.parseInt(PropertyReader.getInstance().getProperty("security.ip.lock.unpenalized.failed.attempts"));
	private static final int LOCKOUT_MAX_PERIOD 		  = Integer.parseInt(PropertyReader.getInstance().getProperty("security.lockout.max"));
	
	private static final Logger LOGGER = Logger.getLogger(IpLockoutManager.class.getName());
	
	private String ipAddress;
	private Timestamp lastFailureDate;
	private int failCount;
	
	// The cooldown period is the amount of time that must have passed
	// before a user can be removed from the lockout table.
	// By design it is equal to the number of times login has failed (failcount) times 5, in minutes
	// The multiplier is however configurable
	private long cooldownPeriod;
	
	public IpLockoutManager(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public synchronized boolean isLocked() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(SELECT_LOCKOUT_STATEMENT);
		ResultSet results;
		long lockoutThresholdTime; 
		
		statement.setString(1, ipAddress);
		
		results = statement.executeQuery();
		
		// The IP address hasn't been locked out - No such entry exists in the table
		if(!results.first()) {
			LOGGER.info("IP " + ipAddress + " not present in IP lockout table. Returning unlocked.");
			return false;
		}
		
		lastFailureDate = results.getTimestamp("lastfailure");
		failCount = results.getInt("failcount");
		cooldownPeriod = getCooldownPeriod(failCount);
		
		// The time stamp when the user will no longer be locked
		lockoutThresholdTime = lastFailureDate.getTime() + cooldownPeriod;
		
		if(failCount > LOCKOUT_UNPENALIZED_ATTEMPTS && System.currentTimeMillis() < lockoutThresholdTime) {
			logLockedStatus(ipAddress, failCount, lockoutThresholdTime, System.currentTimeMillis());
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
	
	public synchronized static void addLock(String ipAddress) throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(ADD_LOCKOUT_STATEMENT);
		Date date 							= new Date(new java.util.Date().getTime());
		Timestamp timestamp 				= new Timestamp(date.getTime());
		
		statement.setString(1, ipAddress);
		statement.setTimestamp(2, timestamp);
		statement.setTimestamp(3, timestamp);
		
		statement.executeUpdate();
		LOGGER.info("Added IP lock for IP " + ipAddress);
	}
	
	public synchronized static void deleteLock(String ipAddress) throws SQLException, ClassNotFoundException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(DELETE_STATEMENT);
		
		statement.setString(1, ipAddress);
		
		statement.executeUpdate();
		LOGGER.info("Deleted lock for IP " + ipAddress);
	}
	
	private void logLockedStatus(String ip, int failCount, long lockoutThresholdTime, long currentTime) {
		String message = "IP %s has been banned: Current fail count is %d (unpenalized=%d). IP will be unlocked after %d. It is now %d.";
		
		message = String.format(message, ip, failCount, LOCKOUT_UNPENALIZED_ATTEMPTS, lockoutThresholdTime, currentTime);
		LOGGER.info(message);
	}
}
