package bg.tsarstva.follow.api.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Code to connect to the Follow database
 * @author ivaylo.marinkov
 *
 */

public class DatabaseConnector {
	
	private static DatabaseConnector databaseConnector = null;
	private Connection connection;
	
	private DatabaseConnector() throws ClassNotFoundException, SQLException {
		PropertyReader propertyReader = PropertyReader.getInstance();
		String username = propertyReader.getProperty("database.user");
		String password = propertyReader.getProperty("database.password");
		
	    Class.forName("com.mysql.jdbc.Driver");
	    connection = DriverManager.getConnection(UrlBuilder.getJdbcUrl(), username, password);
	}
	
	public static DatabaseConnector getInstance() throws ClassNotFoundException, SQLException {
		if(databaseConnector == null) {
			databaseConnector = new DatabaseConnector();
		}
		
		return databaseConnector;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
