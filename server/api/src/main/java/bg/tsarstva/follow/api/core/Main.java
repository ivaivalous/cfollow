package bg.tsarstva.follow.api.core;

import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * API starting point
 * @author ivaylo.marinkov
 *
 */

public class Main {
	private static final String URL = UrlBuilder.getApplicationUrl();
	private static final String WEBADMIN_PACKAGE = "bg.tsarstva.follow.api.webadmin.endpoint";
	private static final String CLIENT_PACKAGE   = "bg.tsarstva.follow.api.client.endpoint";
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static ResourceConfig resourceConfig = null;
	
	private Main() {};
	
    public static HttpServer start() {
        resourceConfig = new ResourceConfig().packages(WEBADMIN_PACKAGE, CLIENT_PACKAGE);
        
        // TODO Support TLS
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(URL), resourceConfig);
    }
    
    public static void main(String[] args) throws IOException {
    	LogHandler.configure();
    	LogHandler.configureGrizzlyLogging();
    	start();
    	
    	LOGGER.info("Follow web API started.");
    	
        while(true) {
        	System.in.read();
        }
    }
}
