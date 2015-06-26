package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

public class GenericFailureResponse extends AbstractResponseBuilder {
	
	JSONObject response;

	public GenericFailureResponse(String failureReason) {
		response = new JSONObject();
		response.accumulate("success", false);
		response.accumulate("failureReason", failureReason);
	}
	
	public JSONObject getResponse() {
		return response;
	}

}
