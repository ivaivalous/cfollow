package bg.tsarstva.follow.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import bg.tsarstva.follow.api.core.PropertyReader;
import bg.tsarstva.follow.api.entity.User;

/**
 * Basically a map of the claims for a user authentication JSON web token
 * @author ivaylo.marinkov
 *
 */

public class UserJwt {
	
	private static final Logger LOGGER = Logger.getLogger(UserJwt.class.getName());
	
	private static final String JWT_ISSUER  = PropertyReader.getInstance().getProperty("jwt.issuer");
	private static final int JWT_TTL        = Integer.parseInt(PropertyReader.getInstance().getProperty("jwt.ttl.hours"));
	private static final String SIGNING_KEY = PropertyReader.getInstance().getProperty("jwt.password");
	
	private User user;
	private Map<String, Object> claimsMap;
	
	public UserJwt(User user) {
		this.user = user;
		claimsMap = new HashMap<String, Object>();
		buildClaimsMap();
	}
	
	private void buildClaimsMap() {
		long issueTime      = System.currentTimeMillis();
		// Time in hours times minutes in an hour times seconds in a minute times milliseconds in a second
		long expirationTime = issueTime + JWT_TTL * 60 * 60 * 1000;
		
		// Registered claims
		claimsMap.put("iss", JWT_ISSUER);
		claimsMap.put("iat", issueTime);
		claimsMap.put("exp", expirationTime);
		
		// Public claims
		claimsMap.put("isadmin", user.isAdmin());
		claimsMap.put("apiKey", user.getApiKey());
		claimsMap.put("username", user.getUserName());
		claimsMap.put("userid", user.getUserId());
		claimsMap.put("email", user.getEmail());
		claimsMap.put("nicename", user.getNiceName());
	}

	public Map<String, Object> getClaimsMap() {
		return claimsMap;
	}
	
	public static String buildUserJwt(User user) {
		UserJwt userJwt = new UserJwt(user);
		Map<String, Object> claimsMap = userJwt.getClaimsMap();
		
		return Jwts.builder().setClaims(claimsMap).signWith(SignatureAlgorithm.HS512, SIGNING_KEY).compact();
	}
	
	public static boolean validateJwt(String jwt) {
		try {
			Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SIGNING_KEY)).parseClaimsJws(jwt).getBody();
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Invalid JWT signature, authentication failed: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public static boolean jwtIsAdmin(String jwt) {
		if(validateJwt(jwt)) {
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SIGNING_KEY)).parseClaimsJws(jwt).getBody();
			return (Boolean)claims.get("isadmin");
		} else {
			return false;
		}
	}
	
	public static Claims validateAndGetClaims(String jwt) {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SIGNING_KEY)).parseClaimsJws(jwt).getBody();
	}
}
