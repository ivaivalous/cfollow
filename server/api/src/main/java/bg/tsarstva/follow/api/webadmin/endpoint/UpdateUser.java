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

import bg.tsarstva.follow.api.database.query.UpdateUserQuery;
import bg.tsarstva.follow.api.security.jwt.UserJwt;
import bg.tsarstva.follow.api.webadmin.response.AuthenticationFailureResponse;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;
import bg.tsarstva.follow.api.webadmin.response.UpdateUserResponseBuilder;

/**
 * Update user API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/updateUser")
public class UpdateUser {
	public UpdateUser() {};
	
	private static final Logger LOGGER = Logger.getLogger(UpdateUser.class.getName());
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(
			@FormParam(value = "username") String username,
			@FormParam(value = "password") String password,
			@FormParam(value = "nicename") String nicename,
			@FormParam(value = "email") String email,
			@FormParam(value = "apiKey") String apiKey,
			@FormParam(value = "isadmin") int isAdmin,
			@FormParam(value = "isdisabled") int isDisabled,
			@FormParam(value = "isdeleted") int isDeleted,
			@HeaderParam(value = "Authorization") String jwt
			) {
		UpdateUserQuery query;
		UpdateUserResponseBuilder responseBuilder;
		
		if(!UserJwt.jwtIsAdmin(jwt)) {
			return Response.status(Status.UNAUTHORIZED).entity(new AuthenticationFailureResponse().getResponse().toString()).build();
		}
		
		try {
			query = new UpdateUserQuery(username, password, nicename, email, apiKey, isAdmin, isDisabled, isDeleted).execute();
			responseBuilder = new UpdateUserResponseBuilder(query);
		} catch(SQLException | ClassNotFoundException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		
    	return Response.ok().entity(responseBuilder.getResponse().toString()).build();
	}
}