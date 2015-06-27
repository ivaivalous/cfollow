package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.UserLoginQuery;

public class UserLoginResponseBuilder extends AbstractResponseBuilder {
	
	UserLoginQuery login;
	
	public UserLoginResponseBuilder(UserLoginQuery login) {
		this.login = login;
	}
	
	private JSONObject buildLoginResponse() {
		JSONObject result = new JSONObject();
		
		result.accumulate("success", true);
		result.accumulate("username", login.getUser().getUserName());
		result.accumulate("nicename", login.getUser().getNiceName());
		result.accumulate("email", login.getUser().getEmail());
		result.accumulate("jwt", getJwt());
		
		return result;
	}

	public JSONObject getResponse() {
		if(!(Boolean)login.getResult()) {
			return new GenericFailureResponse().getResponse();
		} else {
			return buildLoginResponse();
		}
	}
	
	public String getJwt() {
		return login.getJwt();
	}
}
