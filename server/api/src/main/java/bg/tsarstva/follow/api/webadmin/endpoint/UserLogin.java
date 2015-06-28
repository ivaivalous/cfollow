package bg.tsarstva.follow.api.webadmin.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.Request;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bg.tsarstva.follow.api.database.query.UserLoginQuery;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;
import bg.tsarstva.follow.api.webadmin.response.UserLoginResponseBuilder;

/**
 * Reset password confirm API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/userLogin")
public class UserLogin {
	private static final Logger LOGGER = Logger.getLogger(UserLogin.class.getName());
	
	public UserLogin() {};
	
	private String getIpAddress(Request request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		
		if(ipAddress == null) {
			return request.getRemoteAddr();
		} else {
			return ipAddress;
		}
	}
	
	@Context private javax.servlet.http.HttpServletRequest hsr;
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response userLogin(
			@Context Request request, 
			@FormParam("username") String username,
			@FormParam("email") String email,
			@FormParam("password") String password
	) {
		   String ipAddress = getIpAddress(request);
		   
		   boolean usingEmail;
		   UserLoginQuery userLoginQuery;
		   UserLoginResponseBuilder userLoginResponse;
		   
		   request.getRemoteAddr();
		   
		   // If no username has been set in the request we're dealing, by default, with an email address
		   usingEmail = username == null;
		   
		   try {
			   if(usingEmail) {
				  userLoginQuery = new UserLoginQuery(email, password, ipAddress, true).execute();
			   } else {
				   userLoginQuery = new UserLoginQuery(username, password, ipAddress, false).execute();
			   }
			   
			   userLoginResponse = new UserLoginResponseBuilder(userLoginQuery);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.severe("SQL error inserting new user: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		   
		return Response.ok().entity(userLoginResponse.getResponse().toString()).build();
	}
}