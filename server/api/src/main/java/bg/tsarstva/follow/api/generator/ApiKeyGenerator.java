package bg.tsarstva.follow.api.generator;

import java.util.UUID;

/**
 * Generate user API keys
 * @author ivaylo.marinkov
 *
 */

public class ApiKeyGenerator {
	
	private ApiKeyGenerator() {};
	
	public static String generate() {
		// TODO implementation pending
		return UUID.randomUUID().toString();
	}

}
