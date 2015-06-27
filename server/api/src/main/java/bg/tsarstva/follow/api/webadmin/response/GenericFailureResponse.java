package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

public class GenericFailureResponse extends AbstractResponseBuilder {
	
	JSONObject response;
	
	public GenericFailureResponse() {
		this(null);
	}

	public GenericFailureResponse(String failureReason) {
		response = new JSONObject();
		response.accumulate("success", false);
		
		if(failureReason != null) {
			response.accumulate("failureReason", failureReason);
		}
	}
	
	public JSONObject getResponse() {
		return response;
	}

}
