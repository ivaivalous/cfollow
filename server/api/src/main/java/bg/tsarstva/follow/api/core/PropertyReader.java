package bg.tsarstva.follow.api.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Read application properties.
 * @author ivaylo.marinkov
 *
 */

public class PropertyReader {
	
	private static final String PROPERTIES_FILENAME = "follow.properties";
	private static final Logger LOGGER = Logger.getLogger(PropertyReader.class.getName());
	private static PropertyReader propertyReader = null;
	private static Properties properties;
	
	private PropertyReader() {
		try {
			buildProperties();
		} catch(IOException e) {
			LOGGER.severe("Error loading properties: " + e.getMessage());
		}
	};
	
	public static PropertyReader getInstance() {
		if(propertyReader == null) {
			propertyReader = new PropertyReader();
		}
		
		return propertyReader;
	}
	
	private void buildProperties() throws IOException {
		properties = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
		
		if (inputStream != null) {
			properties.load(inputStream);
		} else {
			LOGGER.severe("Configuration file follow.properties not found.");
			throw new FileNotFoundException("Configuration file follow.properties not found.");
		}
	}
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
	
	public boolean isTrue(String name) {
		String resultRaw = getProperty(name).toLowerCase();
		
		return resultRaw.equals(Boolean.TRUE.toString()) || resultRaw.equals("1");
	}
}
