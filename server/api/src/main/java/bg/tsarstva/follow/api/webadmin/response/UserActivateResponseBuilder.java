package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.UserActivateQuery;

public class UserActivateResponseBuilder extends AbstractResponseBuilder {
	
	private boolean activationSuccessful;
	
	public UserActivateResponseBuilder(UserActivateQuery query) {
		activationSuccessful = (Boolean)query.getResult();
	}

	@Override
	public JSONObject getResponse() {
		if(activationSuccessful) {
			return new GenericSuccessResponse().getResponse();
		} else {
			return new GenericFailureResponse("InvalidOrInactiveActivationCode").getResponse();
		}
	}
}