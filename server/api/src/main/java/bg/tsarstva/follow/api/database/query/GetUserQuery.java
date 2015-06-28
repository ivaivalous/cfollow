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
	
	private static final String STATEMENT_USERNAME 	= "select * from `cf_users.data` where username = ?";
	private static final String STATEMENT_EMAIL 	= "select * from `cf_users.data` where email = ?";
	private static final String STATEMENT_USERID 	= "select * from `cf_users.data` where username = ?";
	private static final String STATEMENT_APIKEY 	= "select * from `cf_users.data` where apiKey = ?";
	private static final String PASSWORD_CAUSE 		= " and password = ?;";
	private static ResultSet queryResult;
	
	private String username;
	private String email;
	private String password;
	private int userid;
	boolean useUserId;
	boolean useEmail;
	
	public GetUserQuery(String username) {
		this.username = username;
		useUserId = false;
		useEmail = false;
	}
	
	public GetUserQuery(String email, boolean usingEmail) {
		if(usingEmail) {
			this.email = email;
			useEmail = true;
		} else {
			this.username = email;
			useUserId = false;
			useEmail = false;
		}
	}
	
	public GetUserQuery(int userid) {
		this.userid = userid;
		useUserId = true;
		useEmail = false;
	}
	
	public GetUserQuery() {
		useUserId = false;
		useEmail = false;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	private String getStatementTemplate() {
		String statement;
		
		if(useEmail) {
			statement = STATEMENT_EMAIL;
		} else if(useUserId) {
			statement = STATEMENT_USERID;
		} else {
			statement = STATEMENT_USERNAME;
		}
		
		if(password != null) {
			statement += PASSWORD_CAUSE;
		}
		
		return statement;
	}
	
	public synchronized GetUserQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(getStatementTemplate());
		
		if(useUserId) {
			statement.setInt(1, userid);
		} else if(useEmail) {
			statement.setString(1, email);
		} else {
			statement.setString(1, username);
		}
		
		if(password != null) {
			statement.setString(2, password);
		}
		
		queryResult = statement.executeQuery();
		return this;
	}
	
	public static synchronized User getByApiKey(String apiKey) throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(STATEMENT_APIKEY);
		ResultSet queryResult;
		
		statement.setString(1, apiKey);
		
		queryResult = statement.executeQuery();
		return toUser(queryResult);
	}
	
	private static User toUser(ResultSet queryResult) {
		User user = new User();
		
		try {
			queryResult.first();
			
			user.setUserId(queryResult.getInt("userid"));
			user.setUserName(queryResult.getString("username"));
			user.setNiceName(queryResult.getString("nicename"));
			user.setPasswordHash(queryResult.getString("password"));
			user.setEmail(queryResult.getString("email"));
			user.setApiKey(queryResult.getString("apiKey"));
			user.setIsAdmin(queryResult.getBoolean("isadmin"));
			user.setIsDisabled(queryResult.getBoolean("isdisabled"));
			user.setIsDeleted(queryResult.getBoolean("isdeleted"));
			user.setIsActivated(queryResult.getBoolean("isactivated"));
		} catch(SQLException e) {
			user.setIsValid(false);
		}
		
		return user;
	}
	
	public User getResult() {
		return toUser(queryResult);
	}
}
