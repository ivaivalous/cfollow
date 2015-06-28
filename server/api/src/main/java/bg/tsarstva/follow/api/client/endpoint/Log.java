package bg.tsarstva.follow.api.client.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bg.tsarstva.follow.api.database.query.LogQuery;
import bg.tsarstva.follow.api.entity.NoSuchUserException;
import bg.tsarstva.follow.api.entity.client.log.InvalidClientLogJsonException;
import bg.tsarstva.follow.api.webadmin.response.AuthenticationFailureResponse;
import bg.tsarstva.follow.api.webadmin.response.GenericSuccessResponse;
import bg.tsarstva.follow.api.webadmin.response.InputFormatFailureResponse;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;

/**
 * Client logging API
 * @author ivaylo.marinkov
 *
 */

@Path("client/log")
public class Log {
	
	private static final Logger LOGGER = Logger.getLogger(Log.class.getName());
	
	public Log() {}
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response log(String message) {
		
		try {
			new LogQuery(message).execute();
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		} catch (InvalidClientLogJsonException formatException) {
			return Response.status(Status.BAD_REQUEST).entity(new InputFormatFailureResponse().getResponse().toString()).build();
		} catch (NoSuchUserException userException) {
			return Response.status(Status.UNAUTHORIZED).entity(new AuthenticationFailureResponse().getResponse().toString()).build();
		}
		
		return Response.ok().entity(new GenericSuccessResponse().getResponse().toString()).build();
	}
}
