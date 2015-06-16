package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.CreateUserQuery;

public class CreateUserResponseBuilder extends AbstractResponseBuilder {
	
	public CreateUserResponseBuilder(CreateUserQuery queryResult) {
		// TODO Check if failure
	}

	public JSONObject getResponse() {
		return new GenericSuccessResponse().getResponse();
	}
}
