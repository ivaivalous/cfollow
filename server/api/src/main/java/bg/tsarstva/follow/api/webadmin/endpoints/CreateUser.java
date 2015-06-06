package bg.tsarstva.follow.api.webadmin.endpoints;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Create user API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/createUser")
public class CreateUser {
	private CreateUser() {};
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	public Response createUser(String message) {
    	return Response.ok().entity(message).build();
	}
}