package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;
import bg.tsarstva.follow.api.database.query.UpdateUserQuery;

public class UpdateUserResponseBuilder extends AbstractResponseBuilder {
	
	public UpdateUserResponseBuilder(UpdateUserQuery queryResult) {
		// TODO Check if failure
	}

	public JSONObject getResponse() {
		return new GenericSuccessResponse().getResponse();
	}
}