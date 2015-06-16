package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import bg.tsarstva.follow.api.core.DatabaseConnector;

/**
 * 
 * @author ivaylo.marinkov
 *
 */

public class CreateUserQuery extends AbstractQuery {
	
	private static final String STATEMENT = "INSERT INTO `cfollow`.`cf_users.data` (`username`, `password`, `nicename`, `email`, `apiKey`, `isactivated`, `isadmin`, `isdisabled`, `isdeleted`) VALUES (?, ?, ?, ?, ?, '1', '0', '0', '0');";
	private static int queryResult;
	
	private String username;
	private String password;
	private String nicename;
	private String email;
	private String apiKey;
	
	public CreateUserQuery(String username, String password, String nicename, String email, String apiKey) {
		this.username = username;
		this.password = password;
		this.nicename = nicename;
		this.email = email;
		this.apiKey = apiKey;
	}

	public CreateUserQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(STATEMENT);
		
		statement.setString(1, username);
		
		// TODO don't save pass, hash it instead
		statement.setString(2, password);
		
		statement.setString(3, nicename);
		statement.setString(4, email);
		statement.setString(5, apiKey);
		
		queryResult = statement.executeUpdate();
		return this;
	}

	public Integer getResult() {
		return queryResult;
	}
}
