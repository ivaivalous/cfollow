package bg.tsarstva.follow.api.webadmin.endpoint;

import io.jsonwebtoken.Claims;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bg.tsarstva.follow.api.database.query.GetClientRecordsQuery;
import bg.tsarstva.follow.api.security.jwt.UserJwt;
import bg.tsarstva.follow.api.webadmin.response.AuthenticationFailureResponse;
import bg.tsarstva.follow.api.webadmin.response.GetClientConnectionsResponse;
import bg.tsarstva.follow.api.webadmin.response.SqlErrorResponse;

/**
 * https://github.com/ivaivalous/cfollow/wiki/%5BSPEC%5D-Follow-API-WebAdmin-Requests-Specification
 * @author ivaylo.marinkov
 *
 */

@Path("webadmin/getClientConnections")
public class GetClientConnections {
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getClientLog(
			@QueryParam("dateFrom") String dateFrom,
			@QueryParam("dateTo") String dateTo,
			@QueryParam("count") String count,
			@HeaderParam(value = "Authorization") String jwt
			) {
		
		Claims claims;
		int userId;
		long dateFromLong = getDateLong(dateFrom);
		long dateToLong = getDateLong(dateTo);
		int countInt = getCountInteger(count);
		GetClientRecordsQuery query;
		GetClientConnectionsResponse response;
		
		if(!UserJwt.validateJwt(jwt)) {
			return Response.status(Status.UNAUTHORIZED).entity(new AuthenticationFailureResponse().getResponse().toString()).build();
		} else {
			claims = UserJwt.validateAndGetClaims(jwt);
		}
		
		userId = (int)claims.get("userid");
		
		try {
			query = new GetClientRecordsQuery("connections", userId);
			
			query.setCount(countInt);
			query.setDateFrom(dateFromLong);
			query.setDateTo(dateToLong);
			
			query.execute();
			response = new GetClientConnectionsResponse(query);
		}  catch(SQLException | ClassNotFoundException e) {
			return Response.serverError().entity(new SqlErrorResponse().getResponse()).build();
		}
		
		return Response.ok().entity(response.getResponse().toString()).build();
	}
	
	private long getDateLong(String input) {
		if(input == null || input.isEmpty()) {
			return -1;
		}
		try {
			return Long.parseLong(input);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	
	private int getCountInteger(String input) {
		if(input == null || input.isEmpty()) {
			return -1;
		}
		try {
			return Integer.parseInt(input);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
}
