package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.entity.NoSuchUserException;
import bg.tsarstva.follow.api.entity.User;
import bg.tsarstva.follow.api.entity.client.log.ClientLocationMessage;
import bg.tsarstva.follow.api.entity.client.log.ClientMessage;
import bg.tsarstva.follow.api.entity.client.log.InvalidClientLogJsonException;

public class MessageQuery extends AbstractQuery {
	
	private static final String INSERT_CONNECTION_STATEMENT = "INSERT INTO `cfollow`.`cf_client.connections` (userid, date, ip, ssid) VALUES (?, ?, ?, ?);";
	private static final String INSERT_LOCATION_STATEMENT = "INSERT INTO `cfollow`.`cf_client.positions` (userid, date, latitude, longitude) VALUES (?, ?, ?, ?);";
	
	private JSONObject messagesMessage;
	private ClientMessage clientMessage;
	private String ip;
	private User user;
	private boolean valid;
	
	public MessageQuery(String message, String ip) {
		this.ip = ip;
		
		try {
			messagesMessage = new JSONObject(message);
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

	public MessageQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement connectionStatement = databaseConnector.getConnection().prepareStatement(INSERT_CONNECTION_STATEMENT);
		PreparedStatement locationStatement = databaseConnector.getConnection().prepareStatement(INSERT_LOCATION_STATEMENT);
		String apiKey;
		
		if(!valid) {
			throw new InvalidClientLogJsonException();
		}
		
		// Fail on invalid input JSON
		try {
			clientMessage = new ClientMessage(messagesMessage);
		} catch(JSONException e) {
			throw new InvalidClientLogJsonException();
		}
		
		apiKey = clientMessage.getApiKey();
		setUser(apiKey);
		
		// Fail on non-existent user
		if(!user.getIsValid()) {
			throw new NoSuchUserException();
		}
		
		// Add connection information
		connectionStatement.setInt(1, user.getUserId());
		connectionStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
		connectionStatement.setString(3, ip);
		connectionStatement.setString(4, clientMessage.getSsid());
		
		connectionStatement.execute();
		
		// Add positions information
		for(ClientLocationMessage message : clientMessage.getClientLocationMessages()) {
			locationStatement.setInt(1, user.getUserId());
			locationStatement.setTimestamp(2, new Timestamp(message.getDate()));
			locationStatement.setDouble(3, message.getLatitude());
			locationStatement.setDouble(4, message.getLongitude());
			locationStatement.addBatch();
		}
		
		locationStatement.executeBatch();
		
		return this;
	}

	@Override
	public Object getResult() {
		return null;
	}
}