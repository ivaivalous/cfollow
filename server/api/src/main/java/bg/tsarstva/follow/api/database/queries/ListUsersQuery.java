package bg.tsarstva.follow.api.database.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bg.tsarstva.follow.api.core.DatabaseConnector;

/**
 * SQL query associated with the listUsers API
 * @author ivaylo.marinkov
 *
 */

public class ListUsersQuery {
	
	private static final String STATEMENT = "select * from `cf_users.data` where isdeleted = 0";
	private static ResultSet queryResult;
	
	public ListUsersQuery() {};
	
	public ListUsersQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(STATEMENT);
		
		queryResult = statement.executeQuery();
		return this;
	}
	
	public ResultSet getResult() {
		return queryResult;
	}
}
