package bg.tsarstva.follow.api.entity;

/**
 * A user of the Follow system
 * @author ivaylo.marinkov
 *
 */

public class User {
	
	private int userId;
	private String userName;
	private String passwordHash;
	private String niceName;
	private String email;
	private String apiKey;
	private boolean isActivated;
	private boolean isAdmin;
	private boolean isDisabled;
	private boolean isDeleted;
	
	private boolean isValid = true;
	
	public User() {
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setNiceName(String niceName) {
		this.niceName = niceName;
	}
	
	public String getNiceName() {
		return niceName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setIsActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	
	public boolean isActivated() {
		return isActivated;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	
	public boolean isDisabled() {
		return isDisabled;
	}
	
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public boolean getIsValid() {
		return isValid;
	}
}
