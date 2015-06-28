package bg.tsarstva.follow.api.webadmin.endpoint;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bg.tsarstva.follow.api.database.query.UserRegisterQuery;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;
import bg.tsarstva.follow.api.webadmin.response.UserRegisterResponseBuilder;

/**
 * User register API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/userRegister")
public class UserRegister {
	private static final Logger LOGGER = Logger.getLogger(UserRegister.class.getName());
	
	public UserRegister() {};
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
	public Response userRegister(
			@FormParam(value = "email") String email,
			@FormParam(value = "nicename") String nicename,
			@FormParam(value = "password") String password
	) {
		UserRegisterQuery queryResult;
		UserRegisterResponseBuilder userRegisterResponse;
		
		try {
			queryResult = new UserRegisterQuery(email, password, nicename).execute();
			userRegisterResponse = new UserRegisterResponseBuilder(queryResult);
		} catch(SQLException | ClassNotFoundException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse().toString()).build();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.severe("Algorithm error creating password hash " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse().toString()).build();
		}
		
		// TODO Not always "ok"
    	return Response.ok().entity(userRegisterResponse.getResponse().toString()).build();
	}
}