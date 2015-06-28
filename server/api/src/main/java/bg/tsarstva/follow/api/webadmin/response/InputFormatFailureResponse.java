package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

public class InputFormatFailureResponse extends AbstractResponseBuilder {
	
	JSONObject response;

	public InputFormatFailureResponse() {
		response = new JSONObject();
		response.accumulate("success", false);
		response.accumulate("failureReason", "InvalidInputFormat");
	}
	
	public JSONObject getResponse() {
		return response;
	}

}
