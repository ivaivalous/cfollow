package bg.tsarstva.follow.api.webadmin.endpoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Admin login API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/adminLogin")
public class AdminLogin {
	public AdminLogin() {};
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	public Response adminLogin(String message) {
    	return Response.ok().entity(message).build();
	}
}