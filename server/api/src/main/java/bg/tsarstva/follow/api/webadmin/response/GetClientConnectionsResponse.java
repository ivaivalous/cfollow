package bg.tsarstva.follow.api.webadmin.response;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.GetClientRecordsQuery;

public class GetClientConnectionsResponse extends AbstractResponseBuilder {
	
	JSONObject response;
	private ResultSet result;
	
	public GetClientConnectionsResponse(GetClientRecordsQuery result) throws JSONException, SQLException {
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
			innerArrayElement.accumulate("ip", result.getString("ip"));
			innerArrayElement.accumulate("ssid", result.getString("ssid"));
			
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