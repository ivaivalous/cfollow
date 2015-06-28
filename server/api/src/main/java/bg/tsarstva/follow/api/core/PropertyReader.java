package bg.tsarstva.follow.api.core;

import java.io.File;
import java.io.FileInputStream;
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
	private static final String PROPERTIES_OVERRIDE	= "./follow.properties";
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
	
	private boolean doesOverrideFileExist() {
		File file = new File(PROPERTIES_OVERRIDE);
		return file.exists() && !file.isDirectory();
	}
	
	private void buildProperties() throws IOException {
		InputStream inputStream         = null;
		FileInputStream fileInputStream = null;
		properties = new Properties();
		
		if(doesOverrideFileExist()) {
			fileInputStream = new FileInputStream(PROPERTIES_OVERRIDE);
		} else {
			LOGGER.info("Using default properties");
			inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
		}
		
		if (inputStream != null) {
			properties.load(inputStream);
		} else if(fileInputStream != null) {
			properties.load(fileInputStream);
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
