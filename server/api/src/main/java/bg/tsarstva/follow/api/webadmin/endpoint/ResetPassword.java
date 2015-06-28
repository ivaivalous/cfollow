package bg.tsarstva.follow.api.webadmin.endpoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Reset password API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/resetPassword")
public class ResetPassword {
	public ResetPassword() {};
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(String message) {
    	return Response.status(Status.NOT_IMPLEMENTED).entity(message).build();
	}
}