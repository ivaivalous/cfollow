package bg.tsarstva.follow.api.validator;

public class UsernameValidator {
	
	// Only use when user is setting user name themselves
	private static final String USERNAME_PATTERN = "([a-z]+[@_\\.]*[a-z]+){3,128}";
	
	private UsernameValidator() {};
	
	public static boolean isValid(String username) {
		return username.matches(USERNAME_PATTERN);
	}
}