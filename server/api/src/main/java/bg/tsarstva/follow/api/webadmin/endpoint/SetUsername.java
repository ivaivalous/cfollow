package bg.tsarstva.follow.api.webadmin.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bg.tsarstva.follow.api.database.query.SetUsernameQuery;
import bg.tsarstva.follow.api.security.jwt.UserJwt;
import bg.tsarstva.follow.api.webadmin.response.AuthenticationFailureResponse;
import bg.tsarstva.follow.api.webadmin.response.SetUsernameResponseBuilder;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;

/**
 * User register API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/setUsername")
public class SetUsername {
	public SetUsername() {};
	
	private static final Logger LOGGER = Logger.getLogger(SetUsername.class.getName());
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public Response setUsername(
			@FormParam(value = "email") String email,
			@FormParam(value = "newUsername") String newUsername,
			@HeaderParam(value = "Authorization") String jwt
			) {
		SetUsernameQuery query;
		SetUsernameResponseBuilder responseBuilder;
		
		if(!UserJwt.validateJwt(jwt)) {
			return Response.status(Status.UNAUTHORIZED).entity(new AuthenticationFailureResponse().getResponse().toString()).build();
		}
		
		try {
			query = new SetUsernameQuery(email, newUsername).execute();
			responseBuilder = new SetUsernameResponseBuilder(query);
		} catch(SQLException | ClassNotFoundException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		
    	return Response.ok().entity(responseBuilder.getResponse().toString()).build();
	}
}