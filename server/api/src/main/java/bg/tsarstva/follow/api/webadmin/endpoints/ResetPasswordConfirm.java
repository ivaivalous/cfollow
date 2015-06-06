package bg.tsarstva.follow.api.webadmin.endpoints;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Reset password API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/resetPasswordConfirm")
public class ResetPasswordConfirm {
	public ResetPasswordConfirm() {};
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	public Response resetPasswordConfirm(String message) {
    	return Response.ok().entity(message).build();
	}
}