package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.entity.User;

public class SetUsernameQuery extends AbstractQuery {
	
	private static final String STATEMENT = "UPDATE `cfollow`.`cf_users.data` SET `username`= ? WHERE `username` = ?;";
	
	// Expected to match email
	private String currentUserName;
	private String newUserName;
	private boolean operationSuccess;
	
	public SetUsernameQuery(String email, String newUserName) {
		currentUserName = email;
		this.newUserName = newUserName;
	}

	public synchronized SetUsernameQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector;
		PreparedStatement statement;
		User user = new GetUserQuery(currentUserName).execute().getResult();
		
		// User exists and user name is equal to the password - meaning the user registered 
		// through the site
		if(user.getIsValid() && user.getUserName().equals(user.getEmail())) {
			databaseConnector = DatabaseConnector.getInstance();
			statement = databaseConnector.getConnection().prepareStatement(STATEMENT);
			
			statement.setString(1, newUserName);
			statement.setString(2, currentUserName);
			
			statement.executeUpdate();
			operationSuccess = true;
		} else {
			operationSuccess = false;
		}
		
		return this;
	}

	public Object getResult() {
		return operationSuccess;
	}

}
