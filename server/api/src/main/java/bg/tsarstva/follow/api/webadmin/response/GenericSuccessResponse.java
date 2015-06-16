package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

public class GenericSuccessResponse extends AbstractResponseBuilder {
	
	JSONObject response;

	public GenericSuccessResponse() {
		response = new JSONObject();
		response.accumulate("success", true);
	}
	
	public JSONObject getResponse() {
		return response;
	}

}
