package bg.tsarstva.follow.api.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {
	
	private static final String LOG_OUTPUT_FILE_PATH     = PropertyReader.getInstance().getProperty("logging.location");
	private static final String GRIZZLY_OUTPUT_FILE_PATH = PropertyReader.getInstance().getProperty("logging.grizzly.location");	
	
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
	
	public static void configureGrizzlyLogging() throws SecurityException, IOException {
		Logger grizzlyLogger = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
		FileHandler fileHandler = new FileHandler(GRIZZLY_OUTPUT_FILE_PATH, false);
		
		grizzlyLogger.setLevel(Level.FINE);
		grizzlyLogger.setUseParentHandlers(false);
		fileHandler.setLevel(Level.ALL);
		grizzlyLogger.addHandler(fileHandler);
	}
}