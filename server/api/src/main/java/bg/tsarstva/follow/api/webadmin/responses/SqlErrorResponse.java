package bg.tsarstva.follow.api.webadmin.responses;

import org.json.JSONObject;

/**
 * API response in case of an SQL error
 * @author ivaylo.marinkov
 *
 */

public class SqlErrorResponse {

	private SqlErrorResponse() {};
	
	public static JSONObject getDefaultError() {
		JSONObject response = new JSONObject();
		
		response.append("success", false);
		
		return response;
	}
}
