package bg.tsarstva.follow.api.entity.client.log;

/**
 * A single log entry within a larger client log message
 * @author ivaylo.marinkov
 *
 */

public class ClientLogMessage {
	
	private long date;
	private String message;
	private ClientLogLevel level;
	
	public ClientLogMessage(long date, String message, ClientLogLevel level) {
		this.date = date;
		this.message = message;
		this.level = level;
	}
	
	public ClientLogMessage(long date, String message, String level) {
		this.date = date;
		this.message = message;
		this.level = ClientLogLevel.valueOf(level);
	}
	
	public long getDate() {
		return date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public ClientLogLevel getLevel() {
		return level;
	}
}
