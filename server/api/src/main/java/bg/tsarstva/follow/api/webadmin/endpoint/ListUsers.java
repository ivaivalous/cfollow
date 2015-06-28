package bg.tsarstva.follow.api.webadmin.endpoint;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bg.tsarstva.follow.api.database.query.ListUsersQuery;
import bg.tsarstva.follow.api.security.jwt.UserJwt;
import bg.tsarstva.follow.api.webadmin.response.AuthenticationFailureResponse;
import bg.tsarstva.follow.api.webadmin.response.ListUsersResponseBuilder;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;

/**
 * Update user API
 * @see https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/listUsers")
public class ListUsers {
	
	private static final Logger LOGGER = Logger.getLogger(ListUsers.class.getName());
	
	public ListUsers() {};
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response listUsers(
			@DefaultValue("0") @QueryParam("from") int startFrom,
			@DefaultValue("0") @QueryParam("to") int endAt,
			@DefaultValue("40") @QueryParam("count") int count,
			@HeaderParam(value = "Authorization") String jwt
	) {
		ListUsersQuery query;
		ListUsersResponseBuilder responseBuilder;
		
		if(!UserJwt.jwtIsAdmin(jwt)) {
			return Response.status(Status.UNAUTHORIZED).entity(new AuthenticationFailureResponse().getResponse().toString()).build();
		}
		
		try {
			query = new ListUsersQuery().execute();
			responseBuilder = new ListUsersResponseBuilder(query);
		} catch(SQLException | ClassNotFoundException e) {
			LOGGER.severe("SQL error building users list: " + e.getMessage());
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		
    	return Response.ok().entity(responseBuilder.getResponse().toString()).build();
	}
}