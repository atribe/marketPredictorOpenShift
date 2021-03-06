package ibd.web.classes;

/* 
 * Created on Feb 21, 2005 
 * 
 */
import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import javax.mail.*;
//import mt.Markets;

/**
 *
 * @author Aaron
 */
public class SendFromGmail {

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private static String emailMsgTxt;
	private static final String emailSubjectTxt = "MarketPredictor.com";
	private static final String emailFromAddress = "user@gmail.com";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String[] sendTo = {"user@gmail.com"};

	/**
	 * 
	 * @throws Exception
	 */
	public static void main() throws Exception {
		Output outputSP500 = VarSP500.currentSP500;
		Output outputNasdaq = VarNasdaq.currentNasdaq;
		Output outputDow = VarDow.currentDow;

		emailMsgTxt = outputSP500.buyOrSellToday + outputNasdaq.buyOrSellToday;

		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		new SendFromGmail().sendSSLMessage(sendTo, emailSubjectTxt,
				emailMsgTxt, emailFromAddress);
		ibd.web.Constants.Constants.logger.info("Successfully sent email to All Users in SendFrom Gmail.java");
		//	System.out.println("Sucessfully Sent mail to All Users");
	}

	/**
	 * 
	 * @param recipients
	 * @param subject
	 * @param message
	 * @param from
	 * @throws MessagingException
	 */
	public void sendSSLMessage(String recipients[], String subject,
			String message, String from) throws MessagingException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");


		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("user", "password");
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type 
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}
}
