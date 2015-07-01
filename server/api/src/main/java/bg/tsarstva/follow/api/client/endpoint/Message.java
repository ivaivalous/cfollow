package bg.tsarstva.follow.api.client.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.Request;

import bg.tsarstva.follow.api.database.query.MessageQuery;
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

@Path("client/message")
public class Message {
	
	private static final Logger LOGGER = Logger.getLogger(Message.class.getName());
	
	public Message() {}
	
	private String getIpAddress(Request request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		
		if(ipAddress == null) {
			return request.getRemoteAddr();
		} else {
			return ipAddress;
		}
	}
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response log(
			@Context Request request,
			String message
		) {
		String ipAddress = getIpAddress(request);
		
		try {
			new MessageQuery(message, ipAddress).execute();
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
