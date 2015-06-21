package bg.tsarstva.follow.api.validator;

/**
 * Validate a password to meet a set of minimum security requirements
 * 
 * Requirements list:
 *   Longer than eight characters
 *   Must not contain the user's email address or name
 *   Must include lower case letters (a-z)
 *   Must include upper case letters (a-Z)
 *   Must include digits (0-9)
 *   Should include special characters
 *   
 *   Not currently included but a should for the future - implement
 *   password expiration.
 *   
 * 
 * @author ivaylo.marinkov
 *
 *
 */

public class PasswordValidator {
	
	private static final String USERNAME_VALIDATOR = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";
	
	private String name;
	private String [] partedName;
	private String email;
	private transient String password;
	
	public PasswordValidator(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		
		breakNameToSegments();
	};
	
	private void breakNameToSegments() {
		partedName = name.split(" ");
	}
	
	public boolean isValid() {
		// Password is invalid if it contains parts of the user's name
		for(String nameSegment : partedName) {
			if(password.contains(nameSegment.trim())) {
				return false;
			}
		}
		
		// Password is invalid if it contains the user's email address
		if(password.contains(email)) {
			return false;
		}
		
		// Finally validate against a regular expression that 
		// the other requirements are met
		return password.matches(USERNAME_VALIDATOR);
	}
}
