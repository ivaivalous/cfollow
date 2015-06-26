package bg.tsarstva.follow.api.webadmin.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bg.tsarstva.follow.api.database.query.UserActivateQuery;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;
import bg.tsarstva.follow.api.webadmin.response.UserActivateResponseBuilder;

/**
 * User activate API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/userActivate")
public class UserActivate {
	private static final Logger LOGGER = Logger.getLogger(UserActivate.class.getName());
	
	public UserActivate() {};
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public Response userActivate(
			@FormParam(value = "userid") int userid,
			@FormParam(value = "token") String token
		) {
		return act(userid, token);
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response userActivateByGet(
			@QueryParam (value = "userid") int userid,
			@QueryParam (value = "token") String token
		) {
		return act(userid, token);
	}
	
	private Response act(int userid, String token) {
		UserActivateQuery query;
		UserActivateResponseBuilder responseBuilder;
		
		try {
			query = new UserActivateQuery(userid, token).execute();
			responseBuilder = new UserActivateResponseBuilder(query);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		
    	return Response.ok().entity(responseBuilder.getResponse().toString()).build();
	}
}