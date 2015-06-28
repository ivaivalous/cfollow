package bg.tsarstva.follow.api.entity.client.log;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An object to represent a log request received from a client device
 * @author ivaylo.marinkov
 *
 */

public class ClientLog {
	
	private String apiKey;
	private JSONObject json;
	private List<ClientLogMessage> messages;
	
	public ClientLog(JSONObject json) {
		this.json = json;
		transform();
	}
	
	private void transform() {
		JSONArray logMessages = json.getJSONArray("log");
		JSONObject arrayEntry;
		ClientLogMessage arrayEntryMessage;
		
		apiKey = json.getString("apiKey");
		messages = new LinkedList<ClientLogMessage>();
		
		for(int i = 0; i < logMessages.length(); i++) {
			arrayEntry = logMessages.getJSONObject(i);
			arrayEntryMessage = new ClientLogMessage(arrayEntry.getLong("date"), arrayEntry.getString("message"), arrayEntry.getString("level"));
			messages.add(arrayEntryMessage);	
		}
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public List<ClientLogMessage> getClientLogMessages() {
		return messages;
	}
}
