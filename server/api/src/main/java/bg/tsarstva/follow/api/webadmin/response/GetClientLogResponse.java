package bg.tsarstva.follow.api.webadmin.response;

/**
 * 
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.GetClientLogQuery;

public class GetClientLogResponse extends AbstractResponseBuilder {
	
	JSONObject response;
	private ResultSet result;
	
	public GetClientLogResponse(GetClientLogQuery result) throws JSONException, SQLException {
		this.result = (ResultSet)result.getResult();
		buildResponse();
	}
	
	private void buildResponse() throws JSONException, SQLException {
		JSONArray logsArray = new JSONArray();
		JSONObject innerArrayElement;
		response = new JSONObject();
		
		while(result.next()) {
			innerArrayElement = new JSONObject();
			
			innerArrayElement.accumulate("date", result.getLong("date"));
			innerArrayElement.accumulate("level", result.getString("level"));
			innerArrayElement.accumulate("message", result.getString("message"));
			
			logsArray.put(innerArrayElement);
		}
		
		response.accumulate("success", true);
		response.accumulate("log", logsArray);
	}

	@Override
	public JSONObject getResponse() {
		return response;
	}
}