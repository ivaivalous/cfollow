package bg.tsarstva.follow.api.database.query;

import java.sql.SQLException;

public abstract class AbstractQuery {
	
	public abstract AbstractQuery execute() throws ClassNotFoundException, SQLException;	
	public abstract Object getResult();
}
