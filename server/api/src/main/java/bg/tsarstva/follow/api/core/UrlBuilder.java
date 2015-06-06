package bg.tsarstva.follow.api.core;

/**
 * Utility class to build server URL
 * @author ivaylo.marinkov
 *
 */

public class UrlBuilder {
	
	private UrlBuilder() {};
	
	public static String getUrl() {
		PropertyReader propertyReader = new PropertyReader();
		String protocol = getProtocol(propertyReader);
		String host = propertyReader.getProperty("application.host");
		String port = propertyReader.getProperty("application.port");
		
		return protocol + host + ':' + port;
	}
	
	private static String getProtocol(PropertyReader propertyReader) {
		if(propertyReader.isTrue("application.tls")) {
			return "https://";
		} else {
			return "http://";
		}
	}
}
