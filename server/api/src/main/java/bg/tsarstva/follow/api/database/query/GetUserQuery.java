package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.entity.User;

/**
 * SQL query for getting a single user, not exposed
 * @author ivaylo.marinkov
 *
 */

public class GetUserQuery extends AbstractQuery {
	
	private static final String STATEMENT_USERNAME = "select * from `cf_users.data` where username = ?";
	private static final String STATEMENT_USERID = "select * from `cf_users.data` where userid = ?";
	private static ResultSet queryResult;
	
	private String username;
	private int userid;
	boolean useUserId;
	
	public GetUserQuery(String username) {
		this.username = username;
		useUserId = false;
	};
	
	public GetUserQuery(int userid) {
		this.userid = userid;
		useUserId = true;
	};
	
	public synchronized GetUserQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(useUserId ? STATEMENT_USERID : STATEMENT_USERNAME);
		
		if(useUserId) {
			statement.setInt(1, userid);
		} else {
			statement.setString(1, username);
		}
		
		queryResult = statement.executeQuery();
		return this;
	}
	
	public User getResult() {
		User user = new User();
		
		try {
			queryResult.first();
			
			user.setUserId(queryResult.getInt("userid"));
			user.setUserName(queryResult.getString("username"));
			user.setNiceName(queryResult.getString("nicename"));
			user.setEmail(queryResult.getString("email"));
			user.setApiKey(queryResult.getString("apiKey"));
			user.setIsAdmin(queryResult.getBoolean("isadmin"));
			user.setIsDisabled(queryResult.getBoolean("isdisabled"));
			user.setIsDeleted(queryResult.getBoolean("isdeleted"));
			user.setIsActivated(queryResult.getBoolean("isactivated"));
		} catch(SQLException e) {
			// TODO log error
			user.setIsValid(false);
		}
		
		return user;
	}
}
