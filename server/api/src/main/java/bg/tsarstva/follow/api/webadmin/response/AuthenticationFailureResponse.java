package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

public class AuthenticationFailureResponse extends GenericFailureResponse {
	
	JSONObject response;
	
	public AuthenticationFailureResponse() {
		response = new JSONObject();
		response.accumulate("success", false);
		response.accumulate("failureReason", "AuthenticationFailed");
	}
	
	public JSONObject getResponse() {
		return response;
	}
}
