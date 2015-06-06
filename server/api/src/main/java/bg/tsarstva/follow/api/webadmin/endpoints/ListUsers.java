package bg.tsarstva.follow.api.webadmin.endpoints;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Update user API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/listUsers")
public class ListUsers {
	private ListUsers() {};
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response listUsers(
			@DefaultValue("0") @QueryParam("from") int startFrom,
			@DefaultValue("0") @QueryParam("to") int endAt,
			@DefaultValue("40") @QueryParam("count") int count
	) {
    	return Response.ok().entity("{}").build();
	}
}