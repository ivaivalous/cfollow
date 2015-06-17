package bg.tsarstva.follow.api.webadmin.response;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.ListUsersQuery;

/**
 * Build a response to the listUsers query
 * @author ivaylo.marinkov
 *
 */

public class ListUsersResponseBuilder extends AbstractResponseBuilder {
	
	private JSONObject response;

	public ListUsersResponseBuilder(ListUsersQuery queryResult) throws SQLException {
		ResultSet result = queryResult.getResult();
		JSONArray usersArray = new JSONArray();
		JSONObject usersArrayEntry;
		response = new JSONObject();
		
		response.accumulate("success", true);
		
		// TODO update values
		response.accumulate("requested", 1);
		response.accumulate("from", 0);
		response.accumulate("to", 40);
		response.accumulate("total", 1);
		
		while(result.next()) {
			usersArrayEntry = new JSONObject();
			
			// TODO Have this replaced by a list of User objects
			usersArrayEntry.accumulate("username", result.getString("username"));
			usersArrayEntry.accumulate("nicename", result.getString("nicename"));
			usersArrayEntry.accumulate("email", result.getString("email"));
			usersArrayEntry.accumulate("apiKey", result.getString("apiKey"));
			usersArrayEntry.accumulate("isadmin", result.getBoolean("isadmin"));
			usersArrayEntry.accumulate("isdisabled", result.getBoolean("isdisabled"));
			
			usersArray.put(usersArrayEntry);
		}
		
		response.append("users", usersArray);
	}
	
	public JSONObject getResponse() {
		return response;
	}
}
