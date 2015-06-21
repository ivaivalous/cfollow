package bg.tsarstva.follow.api.validator;

public class EmailAddressValidator {
	
	/*
	 * Note - this won't validate IDN domains.
	 * If dealing with such, make sure you send this method international data in puny code,
	 * like ivo@xn--b1alt.xn--j1aef
	 */
	private static final String EMAIL_VALIDATOR = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	private EmailAddressValidator() {};
	
	public static boolean isValid(String emailAddress) {
		return emailAddress.matches(EMAIL_VALIDATOR);
	}
}