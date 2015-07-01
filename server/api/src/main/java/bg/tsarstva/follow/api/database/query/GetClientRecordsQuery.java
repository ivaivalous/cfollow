package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import bg.tsarstva.follow.api.core.DatabaseConnector;

public class GetClientRecordsQuery extends AbstractQuery {
	
	private static final String STATEMENT_LOGS        = "select * from cfollow.`cf_client.logs` where userid = ? and date > ? and date < ? limit ?;";
	private static final String STATEMENT_CONNECTIONS = "select * from cfollow.`cf_client.connections` where userid = ? and date > ? and date < ? limit ?;";
	private static final String STATEMENT_POSITIONS   = "select * from cfollow.`cf_client.positions` where userid = ? and date > ? and date < ? limit ?;";
	private static final int MAX_LIMIT    = 100000;
	
	private static ResultSet queryResult;
	private int userId;
	private int count;
	private long dateTo;
	private long dateFrom;
	private String tableName;
	
	public GetClientRecordsQuery(String tableName, int userId) {
		this.tableName = tableName;
		this.userId = userId;
		count 		= -1;
		dateTo 		= -1;
		dateFrom 	= -1;
	}
	
	private String getStatement(String tableName) {
		String tableNameNormalized = tableName.toLowerCase().trim();
		
		if(tableNameNormalized.equals("logs")) {
			return STATEMENT_LOGS;
		} else if(tableNameNormalized.equals("connections")) {
			return STATEMENT_CONNECTIONS;
		} else if(tableNameNormalized.equals("positions")) {
			return STATEMENT_POSITIONS;
		}
		
		return null;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setDateTo(long dateTo) {
		this.dateTo = dateTo;
	}
	
	public void setDateFrom(long dateFrom) {
		this.dateFrom = dateFrom;
	}

	@Override
	public GetClientRecordsQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(getStatement(tableName));
		 
		statement.setInt(1, userId);
		
		if(dateFrom == -1) {
			statement.setTimestamp(2, new Timestamp(0));
		} else {
			statement.setTimestamp(2, new Timestamp(dateFrom));
		}
		
		if(dateTo == -1) {
			statement.setTimestamp(3, new Timestamp(new Date().getTime()));
		} else {
			statement.setTimestamp(3, new Timestamp(dateTo));
		}
		
		if(count == -1) {
			statement.setInt(4, MAX_LIMIT);
		} else {
			statement.setInt(4, count);
		}
		
		queryResult = statement.executeQuery();
		return this;
	}

	@Override
	public Object getResult() {
		return queryResult;
	}
}
