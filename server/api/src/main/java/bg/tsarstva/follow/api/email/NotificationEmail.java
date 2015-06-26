package bg.tsarstva.follow.api.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import bg.tsarstva.follow.api.core.PropertyReader;

public class NotificationEmail {
	private String addressee;
	private String from = PropertyReader.getInstance().getProperty("email.noreply.address");
	private String smtpHost = PropertyReader.getInstance().getProperty("email.smtp.host");
	private String smtpPort = PropertyReader.getInstance().getProperty("email.smtp.port");
	private String smtpUser = PropertyReader.getInstance().getProperty("email.smtp.username");
	private transient String smtpPassword = PropertyReader.getInstance().getProperty("email.smtp.password");
	Session session;
	
	String subject;
	private String message;
	private MimeMessage mailMessage;
	
	public NotificationEmail(String addressee, String subject, String message) {
		this.addressee = addressee;
		this.subject = subject;
		this.message = message;
	}
	
	private void buildMessage() throws MessagingException {
		Properties properties = System.getProperties();
		
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.setProperty("mail.smtp.port", smtpPort);
		properties.setProperty("mail.smtp.user", smtpUser);
		properties.setProperty("mail.smtp.auth", "true");
		//properties.setProperty("mail.smtp.starttls.enable", "true");
		
		session = Session.getDefaultInstance(properties);
		
		mailMessage = new MimeMessage(session);
		mailMessage.setFrom(new InternetAddress(from));
		mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(addressee));
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
	}
	
	@SuppressWarnings("static-access")
	public void send() throws MessagingException {
		Transport transport;
		
		buildMessage();
		transport = session.getTransport("smtp");
		transport.connect(smtpHost, smtpUser, smtpPassword);
		mailMessage.saveChanges();
		
		transport.send(mailMessage, mailMessage.getAllRecipients());
	}
}
