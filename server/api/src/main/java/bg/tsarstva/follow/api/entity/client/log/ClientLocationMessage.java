package bg.tsarstva.follow.api.entity.client.log;

/**
 * @author ivaylo.marinkov
 *
 */

public class ClientLocationMessage {
	
	private long date;
	private double latitude;
	private double longitude;
	
	public ClientLocationMessage(long date, double latitude, double longitude) {
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public long getDate() {
		return date;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}
