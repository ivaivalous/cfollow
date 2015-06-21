package bg.tsarstva.follow.api.webadmin.response;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.UserRegisterQuery;

/**
 * Response builder for user registration
 * @author ivaylo.marinkov
 *
 */

public class UserRegisterResponseBuilder extends AbstractResponseBuilder {
	
	List<String> invalidFields;
	
	public UserRegisterResponseBuilder(UserRegisterQuery queryResult) {
		invalidFields = queryResult.getInvalidFields();
	}
	
	private JSONObject buildInvalidDataResponseJson() {
		JSONObject response = new JSONObject();
		JSONArray invalidFieldsArray = new JSONArray();
		
		response.accumulate("success", false);
		response.accumulate("failureReason", "dataValidationIssue");
		
		for(String invalidField : invalidFields) {
			invalidFieldsArray.put(invalidField);
		}
		
		response.accumulate("invalidFields", invalidFieldsArray);
		
		return response;
	}

	public JSONObject getResponse() {
		if(!invalidFields.isEmpty()) {
			return buildInvalidDataResponseJson();
		} else {
			return new GenericSuccessResponse().getResponse();					
		}
	}
}