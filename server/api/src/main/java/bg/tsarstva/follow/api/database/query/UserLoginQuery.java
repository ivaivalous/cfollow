package bg.tsarstva.follow.api.database.query;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.logging.Logger;

import bg.tsarstva.follow.api.entity.User;
import bg.tsarstva.follow.api.security.IpLockoutManager;
import bg.tsarstva.follow.api.security.PasswordManager;
import bg.tsarstva.follow.api.security.UserLockoutManager;
import bg.tsarstva.follow.api.security.jwt.UserJwt;

/**
 * API for logging in
 * @author ivaylo.marinkov
 *
 */

public class UserLoginQuery extends AbstractQuery {
	
	private static final Logger LOGGER = Logger.getLogger(UserLoginQuery.class.getName());
	private static final String INVALID_FROM_DATABASE = "User was invalid or missing from database, therefore login failed.";
	private static final String PASSWORD_CORRECT = "Password was correct for user %s.";
	private static final String PASSWORD_INCORRECT = "User %s existed but password was incorrect";
	private static final String USER_VALID = "User %s is in correct status. Checking password.";
	private static final String LOGIN_FAILED = "Login failed for user %s.";
	
	private String username;
	private String email;
	private String password;
	private String ipAddress;
	private boolean usingEmail;
	private boolean loginDeniedByLockout;
	private boolean isUserValid;
	private User user;
	private String jwt;
	
	public UserLoginQuery(String usernameOrEmail, String password, String ipAddress, boolean usingEmail) {
		this.usingEmail = usingEmail;
		
		if(usingEmail) {
			this.email = usernameOrEmail == null ? "" : usernameOrEmail;
		} else {
			this.username = usernameOrEmail == null ? "" : usernameOrEmail;
		}

		this.ipAddress = ipAddress;
		this.password = password == null ? "" : password;
	}

	@Override
	public synchronized UserLoginQuery execute() throws ClassNotFoundException, SQLException {
		IpLockoutManager ipManager = new IpLockoutManager(ipAddress);
		UserLockoutManager userManager = new UserLockoutManager();
		GetUserQuery userQuery;
		
		if(usingEmail) {
			userManager.setEmail(email);
		} else {
			userManager.setUsername(username);
		}
		
		// Make sure no login is attempted if the user or client IP have been locked or blocked out
		if(ipManager.isLocked() || userManager.isLocked()) {
			loginDeniedByLockout = true;
			return this;
		} else {
			loginDeniedByLockout = false;
		}
		
		if(usingEmail) {
			userQuery = new GetUserQuery(email, true);
		} else {
			userQuery = new GetUserQuery(username);
		}
		
		user = userQuery.execute().getResult();
		isUserValid = validateUser(user, ipAddress);
		
		jwt = UserJwt.buildUserJwt(user);
		return this;
	}
	
	private boolean validateUser(User user, String ipAddress) throws ClassNotFoundException, SQLException {
		if(!user.getIsValid()) {
			LOGGER.warning(INVALID_FROM_DATABASE);
			return false;
		}
		
		if(user.isActivated() && !user.isDisabled() && !user.isDeleted()) {			
			LOGGER.info(String.format(USER_VALID, user.getUserName()));
		} else {
			LOGGER.warning(String.format(LOGIN_FAILED, user.getUserName()));
			return false;
		}
			
		try {
			if(PasswordManager.validatePassword(password, user.getPasswordHash())) {
				// Correct password, delete any related locks
				IpLockoutManager.deleteLock(ipAddress);
				UserLockoutManager.deleteLock(user.getUserId());
				
				LOGGER.info(String.format(PASSWORD_CORRECT, user.getUserName()));
			} else {
				IpLockoutManager.addLock(ipAddress);
				UserLockoutManager.addLock(user.getUserId());
				
				LOGGER.warning(String.format(PASSWORD_INCORRECT, user.getUserName()));
				return false;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.warning(e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public Object getResult() {
		return !loginDeniedByLockout && isUserValid;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getJwt() {
		return jwt;
	}
}
