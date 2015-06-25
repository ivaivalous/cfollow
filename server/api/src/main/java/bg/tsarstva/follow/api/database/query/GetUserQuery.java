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
	
	private static final String STATEMENT = "select * from `cf_users.data` where username = ?";
	private static ResultSet queryResult;
	
	private String username;
	
	public GetUserQuery(String username) {
		this.username = username;
	};
	
	public synchronized GetUserQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(STATEMENT);
		
		statement.setString(1, username);
		
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
