package bg.tsarstva.follow.api.entity.client.log;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author ivaylo.marinkov
 *
 */

public class ClientMessage {
	
	private String apiKey;
	private String connectionSsid;
	private JSONObject json;
	private List<ClientLocationMessage> messages;
	
	public ClientMessage(JSONObject json) {
		this.json = json;
		transform();
	}
	
	private void transform() {
		JSONArray locationMessages = json.getJSONObject("positions").getJSONArray("list");
		JSONObject arrayEntry;
		ClientLocationMessage arrayEntryMessage;
		
		apiKey = json.getString("apiKey");
		connectionSsid = json.getString("connectionSsid");
		messages = new LinkedList<ClientLocationMessage>();
		
		for(int i = 0; i < locationMessages.length(); i++) {
			arrayEntry = locationMessages.getJSONObject(i);
			arrayEntryMessage = new ClientLocationMessage(arrayEntry.getLong("date"), arrayEntry.getDouble("latitude"), arrayEntry.getDouble("longitude"));
			messages.add(arrayEntryMessage);	
		}
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public String getSsid() {
		return connectionSsid;
	}
	
	public List<ClientLocationMessage> getClientLocationMessages() {
		return messages;
	}
}