package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.entity.User;

public class UserActivateQuery extends AbstractQuery {
	
	private static final String SELECT_TOKEN_QUERY = "SELECT COUNT(*) FROM cfollow.`cf_users.activation` WHERE userid = ? AND activationtoken = ? AND expiredate >= NOW()";
	private static final String UPDATE_USER_QUERY = "UPDATE cfollow.`cf_users.data` SET isactivated = 1 WHERE userid = ?;";
	private static ResultSet queryResult;
	boolean tokenExists;
	
	private int userId;
	private String providedToken;
	
	public UserActivateQuery(int userId, String providedToken) {
		this.userId = userId;
		this.providedToken = providedToken;
	}
	
	private boolean isActivationSuccessful() throws SQLException, ClassNotFoundException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(SELECT_TOKEN_QUERY);
		
		statement.setInt(1, userId);
		statement.setString(2, providedToken);
		
		queryResult = statement.executeQuery();
		queryResult.next();
		// getInt(1) refers to the COUNT(*) column
		return queryResult.getInt(1) > 0;
	}

	@Override
	public UserActivateQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(UPDATE_USER_QUERY);
		User user = new GetUserQuery(userId).execute().getResult();
		tokenExists = user.isActivated() || isActivationSuccessful();

		if(tokenExists) {
			statement.setInt(1, userId);
			statement.executeUpdate();
			// TODO delete activation table entry on user
		} else {
			// Create (new) activation token
			UserRegisterQuery.createActivationTableRecord(user);
		}
		
		return this;
	}

	@Override
	public Object getResult() {
		return tokenExists;
	}
}
