package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bg.tsarstva.follow.api.core.DatabaseConnector;

/**
 * 
 * @author ivaylo.marinkov
 *
 */

public class UpdateUserQuery extends AbstractQuery {
	
	private static final String STATEMENT = "UPDATE `cfollow`.`cf_users.data` SET `password` = ?, `nicename` = ?, `email` = ?, `apiKey` = ?, `isadmin` = ?, `isdisabled` = ?, `isdeleted` = ? WHERE `username` = ?;";
	private static int queryResult;
	
	private String username;
	private String password;
	private String nicename;
	private String email;
	private String apiKey;
	private int isDisabled;
	private int isDeleted;
	private int isAdmin;
	
	public UpdateUserQuery(String username, String password, String nicename, String email, String apiKey, int isAdmin, int isDisabled, int isDeleted) {
		this.username = username;
		this.password = password;
		this.nicename = nicename;
		this.email = email;
		this.apiKey = apiKey;
		this.isAdmin = isAdmin;
		this.isDisabled = isDisabled;
		this.isDeleted = isDeleted;
	}

	public UpdateUserQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(STATEMENT);
		
		statement.setString(1, password);
		statement.setString(2, nicename);
		statement.setString(3, email);
		statement.setString(4, apiKey);
		statement.setInt(5, isAdmin);
		statement.setInt(6, isDisabled);
		statement.setInt(7, isDeleted);
		statement.setString(8, username);
		
		queryResult = statement.executeUpdate();
		return this;
	}

	public Integer getResult() {
		return queryResult;
	}
}
