package bg.tsarstva.follow.api.database.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import bg.tsarstva.follow.api.core.DatabaseConnector;

public class GetClientLogQuery extends AbstractQuery {
	
	private static final String STATEMENT = "select * from cfollow.`cf_client.logs` where userid = ? and date > ? and date < ? limit ?;";
	private static final int MAX_LIMIT    = 100000;
	
	private static ResultSet queryResult;
	private int userId;
	private int count;
	private long dateTo;
	private long dateFrom;
	
	public GetClientLogQuery(int userId) {
		this.userId = userId;
		count 		= -1;
		dateTo 		= -1;
		dateFrom 	= -1;
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
	public GetClientLogQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement = databaseConnector.getConnection().prepareStatement(STATEMENT);
		
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
