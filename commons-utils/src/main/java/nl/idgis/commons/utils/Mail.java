/**
 * 
 */
package nl.idgis.commons.utils;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Mail helper class.<br>
 * 
 * @author Rob
 *
 */
public class Mail {
	private static final Log logger = LogFactory.getLog(Mail.class);
	
	/**
	 * Create an email body from a map of objects that act as placeholders and a body text template.
	 * @param placeholders Map of objects with String keys<br>
	 * e.g containing <code>"name","Robert"</code>
	 * @param msgTemplate plain text with entries which should be replaced with placeholders.<br>
	 * e.g. text template <br>
	 * <code>"Hello ${name}, here is your mail"</code> <br>
	 * will become (with above placeholder)<br> 
	 * <code>"Hello Robert, here is your mail"</code> <br>
	 * Placeholders entries are written in Velocity Template Language (VTL)<br>
	 * @return the full messagetext, with template entries replaced by proper placeholders.
	 */
	public static String createMsg(Map<String,Object> placeholders, String msgTemplate) {
		if (placeholders == null) {
			return msgTemplate;
		} else {
			VelocityContext context = new VelocityContext();
			for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "", msgTemplate);

			return writer.toString();
		}
	}

	public static void send(String smtpHost, int smtpPort, List<String> toAddresses, String fromAddress, String subject, String msg) throws Exception{
		for (String toAddress : toAddresses) {
			send(smtpHost, smtpPort, toAddress, fromAddress, subject, msg);
		}
	}
	
	/**
	 * Send a mail without authentication.
	 * @param smtpHost
	 * @param smtpPort
	 * @param toAddress
	 * @param fromAddress
	 * @param subject 
	 * @param msg body of the email
	 * @throws Exception EmailException.
	 * @see createMsg
	 */
	public static void send(String smtpHost, int smtpPort, String toAddress, String fromAddress, String subject, String msg) throws Exception{
		send(null,null,smtpHost, smtpPort, toAddress, fromAddress, subject, msg);
	}
	
	/**
	 * Send a mail with authentication.
	 * @param userName
	 * @param password
	 * @param smtpHost
	 * @param smtpPort
	 * @param toAddress
	 * @param fromAddress
	 * @param subject
	 * @param msg body of the email
	 * @throws Exception EmailException
	 * @see createMsg
	 */
	public static void send(String userName, String password, String smtpHost, int smtpPort, String toAddress, String fromAddress, String subject, String msg) throws Exception{
		Email mail = new SimpleEmail();
		if (userName != null && password != null){
			mail.setAuthentication(userName, password);
		}
		mail.setHostName(smtpHost);
		mail.setSmtpPort(smtpPort);
		mail.setFrom(fromAddress);
		mail.setSubject(subject);
		mail.setMsg(msg);					
		mail.addTo(toAddress);
		mail.send();
	}

}
