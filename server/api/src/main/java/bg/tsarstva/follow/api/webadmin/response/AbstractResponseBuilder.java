package bg.tsarstva.follow.api.webadmin.response;

import java.sql.SQLException;

import org.json.JSONObject;

import bg.tsarstva.follow.api.database.query.AbstractQuery;

public abstract class AbstractResponseBuilder {
	
	public AbstractResponseBuilder() {};
	public AbstractResponseBuilder(AbstractQuery queryResult) throws SQLException {};
	public abstract JSONObject getResponse();
}
