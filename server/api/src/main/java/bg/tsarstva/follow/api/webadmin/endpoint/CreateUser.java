package bg.tsarstva.follow.api.webadmin.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bg.tsarstva.follow.api.database.query.CreateUserQuery;
import bg.tsarstva.follow.api.webadmin.response.CreateUserResponseBuilder;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;

/**
 * Create user API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/createUser")
public class CreateUser {
	public CreateUser() {};
	
	private static final Logger LOGGER = Logger.getLogger(CreateUser.class.getName());
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createUser(
			@FormParam(value = "username") String username,
			@FormParam(value = "password") String password,
			@FormParam(value = "nicename") String nicename,
			@FormParam(value = "email") String email,
			@FormParam(value = "apiKey") String apiKey
			) {
		CreateUserQuery query;
		CreateUserResponseBuilder responseBuilder;
		
		try {
			query = new CreateUserQuery(username, password, nicename, email, apiKey).execute();
			responseBuilder = new CreateUserResponseBuilder(query);
		} catch(SQLException | ClassNotFoundException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		
    	return Response.ok().entity(responseBuilder.getResponse().toString()).build();
	}
}