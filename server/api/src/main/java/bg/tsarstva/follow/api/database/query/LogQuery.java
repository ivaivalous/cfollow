package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.entity.NoSuchUserException;
import bg.tsarstva.follow.api.entity.User;
import bg.tsarstva.follow.api.entity.client.log.ClientLog;
import bg.tsarstva.follow.api.entity.client.log.ClientLogMessage;
import bg.tsarstva.follow.api.entity.client.log.InvalidClientLogJsonException;

public class LogQuery extends AbstractQuery {
	
	private static final String INSERT_LOGS_STATEMENT = "INSERT INTO `cfollow`.`cf_client.logs` (userid, level, message, date) VALUES (?, ?, ?, ?);";
	
	private JSONObject logsMessage;
	private ClientLog clientLog;
	private User user;
	private boolean valid;
	
	public LogQuery(String message) {
		try {
			logsMessage = new JSONObject(message);
			valid = true;
		} catch(JSONException e) {
			valid = false;
		}
	}
	
	private void setUser(String apiKey) throws ClassNotFoundException, SQLException {
		user = GetUserQuery.getByApiKey(apiKey);
	}
	
	public boolean isValid() {
		return valid;
	}

	public LogQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(INSERT_LOGS_STATEMENT);
		String apiKey;
		
		if(!valid) {
			throw new InvalidClientLogJsonException();
		}
		
		// Fail on invalid input JSON
		try {
			clientLog = new ClientLog(logsMessage);
		} catch(JSONException e) {
			throw new InvalidClientLogJsonException();
		}
		
		apiKey = clientLog.getApiKey();
		setUser(apiKey);
		
		// Fail on non-existent user
		if(!user.getIsValid()) {
			throw new NoSuchUserException();
		}
		
		for(ClientLogMessage message : clientLog.getClientLogMessages()) {
			statement.setInt(1, user.getUserId());
			statement.setString(2, message.getLevel().name());
			statement.setString(3, message.getMessage());
			statement.setTimestamp(4, new Timestamp(message.getDate()));
			statement.addBatch();
		}
		statement.executeBatch();
		
		return this;
	}

	@Override
	public Object getResult() {
		return null;
	}
}