package bg.tsarstva.follow.api.webadmin.response;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.GetClientRecordsQuery;

public class GetClientPositionsResponse extends AbstractResponseBuilder {
	
	JSONObject response;
	private ResultSet result;
	
	public GetClientPositionsResponse(GetClientRecordsQuery result) throws JSONException, SQLException {
		this.result = (ResultSet)result.getResult();
		buildResponse();
	}
	
	private void buildResponse() throws JSONException, SQLException {
		JSONArray logsArray;
		JSONObject innerArrayElement;
		response = new JSONObject();
		logsArray = new JSONArray();
		
		while(result.next()) {
			innerArrayElement = new JSONObject();
			
			innerArrayElement.accumulate("date", result.getLong("date"));
			innerArrayElement.accumulate("latitude", result.getDouble("latitude"));
			innerArrayElement.accumulate("longitude", result.getDouble("longitude"));
			
			logsArray.put(innerArrayElement);
		}
		
		response.accumulate("success", true);
		response.put("log", logsArray);
	}

	@Override
	public JSONObject getResponse() {
		return response;
	}
}