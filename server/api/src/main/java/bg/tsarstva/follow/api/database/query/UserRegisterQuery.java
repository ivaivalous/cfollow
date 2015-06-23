package bg.tsarstva.follow.api.database.query;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import bg.tsarstva.follow.api.core.DatabaseConnector;
import bg.tsarstva.follow.api.core.PropertyReader;
import bg.tsarstva.follow.api.entity.User;
import bg.tsarstva.follow.api.generator.ApiKeyGenerator;
import bg.tsarstva.follow.api.security.PasswordManager;
import bg.tsarstva.follow.api.validator.EmailAddressValidator;
import bg.tsarstva.follow.api.validator.PasswordValidator;

public class UserRegisterQuery extends AbstractQuery {
	
	private static final String STATEMENT = "INSERT INTO `cfollow`.`cf_users.data` (`username`, `password`, `nicename`, `email`, `apiKey`, `isactivated`, `isadmin`, `isdisabled`, `isdeleted`) VALUES (?, ?, ?, ?, ?, '0', '0', '0', '0');";
	private static final String CREATE_ACTIVATION_RECORD = "INSERT INTO cfollow.`cf_users.activation` (userid, activationtoken, expiredate) VALUES (?, ?, ?);";
	private int queryResult;
	
	private String email;
	private transient String password;
	private String hashedPassword;
	private String nicename;
	private String apiKey;
	private List<String> invalidFields;
	
	public UserRegisterQuery(String email, String password, String nicename) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.email = email;
		this.password = password;
		
		this.nicename = nicename;
		apiKey = ApiKeyGenerator.generate();
		hashedPassword = PasswordManager.createHash(password);
	}
	
	public List<String> getInvalidFields() {
		List<String> invalidFields = new LinkedList<String>();
		PasswordValidator passwordValidator = new PasswordValidator(nicename, email, password);
		
		if(!EmailAddressValidator.isValid(email)) {
			invalidFields.add("email");
		} 
		if(!passwordValidator.isValid()) {
			invalidFields.add("password");
		}
		
		return invalidFields;
	}
	
	private void createActivationTableRecord(User user) throws SQLException, ClassNotFoundException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(CREATE_ACTIVATION_RECORD);
		int userId 							= user.getUserId();
		String token 						= UUID.randomUUID().toString();
		int daysValid 						= Integer.parseInt(PropertyReader.getInstance().getProperty("user.activation.timeout.days"));
		Date date 							= new Date(new java.util.Date().getTime());
		Calendar calendar 					= Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, daysValid);
		date = new Date(calendar.getTimeInMillis());
		
		statement.setInt(1, userId);
		statement.setString(2, token);
		statement.setDate(3, date, calendar);
		
		statement.executeUpdate();
	}

	public UserRegisterQuery execute() throws ClassNotFoundException, SQLException {
		DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
		PreparedStatement statement         = databaseConnector.getConnection().prepareStatement(STATEMENT);
		invalidFields          				= getInvalidFields();
		
		// Some fields contained invalid data
		// don't perform an update
		if(!invalidFields.isEmpty()) {
			return this;
		}
		
		statement.setString(1, email);
		statement.setString(2, hashedPassword);
		statement.setString(3, nicename);
		statement.setString(4, email);
		statement.setString(5, apiKey);
		
		queryResult = statement.executeUpdate();
		
		createActivationTableRecord(new GetUserQuery(email).execute().getResult());
		
		return this;
	}

	public Object getResult() {
		return queryResult;
	}
}