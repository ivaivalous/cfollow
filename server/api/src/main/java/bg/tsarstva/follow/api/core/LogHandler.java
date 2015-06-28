package bg.tsarstva.follow.api.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {
	
	private static final String LOG_OUTPUT_FILE_PATH = PropertyReader.getInstance().getProperty("logging.location");
	private static FileHandler fileHandler;
	private static SimpleFormatter simpleFormatter;
	
	private LogHandler() {}
	
	public static void configure() throws SecurityException, IOException {
	    Logger logger = Logger.getLogger("");

	    fileHandler = new FileHandler(LOG_OUTPUT_FILE_PATH, true);
	    simpleFormatter = new SimpleFormatter();
	    
	    fileHandler.setFormatter(simpleFormatter);
	    logger.addHandler(fileHandler);
	}
}