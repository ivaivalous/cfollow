package bg.tsarstva.follow.api.core;

import java.util.logging.Logger;

/**
 * Utility class to build server URL
 * @author ivaylo.marinkov
 *
 */

public class UrlBuilder {
	private static final Logger LOGGER = Logger.getLogger(UrlBuilder.class.getName());
	
	private UrlBuilder() {};
	
	public static String getApplicationUrl() {
		String protocol = getProtocol(PropertyReader.getInstance());
		String host = PropertyReader.getInstance().getProperty("application.host");
		String port = PropertyReader.getInstance().getProperty("application.port");
		
		return protocol + host + ':' + port;
	}
	
	public static String getJdbcUrl() {
		PropertyReader propertyReader = PropertyReader.getInstance();
		String protocol = "jdbc:mysql://";
		String host = propertyReader.getProperty("database.host");
		String port = propertyReader.getProperty("database.port");
		String databaseName = propertyReader.getProperty("database.name");
		
		return protocol + host + ':' + port + '/' + databaseName;
	}
	
	private static String getProtocol(PropertyReader propertyReader) {
		if(propertyReader.isTrue("application.tls")) {
			return "https://";
		} else {
			LOGGER.warning("Building an unsecure application URL. Consider using TLS.");
			return "http://";
		}
	}
}
