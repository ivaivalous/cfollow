package bg.tsarstva.follow.api.webadmin.response;

import org.json.JSONObject;

/**
 * API response in case of an SQL error
 * @author ivaylo.marinkov
 *
 */

public class SqlErrorResponse extends AbstractResponseBuilder {

	public SqlErrorResponse() {
		
	};

	@Override
	public JSONObject getResponse() {
		JSONObject response = new JSONObject();
		
		response.accumulate("success", false);
		
		return response;
	}
}
