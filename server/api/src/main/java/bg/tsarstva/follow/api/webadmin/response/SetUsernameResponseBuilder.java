package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.SetUsernameQuery;

public class SetUsernameResponseBuilder extends AbstractResponseBuilder {
	
	private boolean operationSuccessful;
	
	public SetUsernameResponseBuilder(SetUsernameQuery operationSuccessful) {
		this.operationSuccessful = (Boolean)operationSuccessful.getResult();
	}

	public JSONObject getResponse() {
		if(operationSuccessful) {
			return new GenericSuccessResponse().getResponse();
		} else {
			return new SqlErrorResponse().getResponse();
		}
	}
}