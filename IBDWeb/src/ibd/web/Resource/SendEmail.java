package ibd.web.Resource;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail
{

	/**
	 * 
	 * @param emailHost
	 * @param fromEmail
	 * @param passKey
	 * @param toEmail
	 * @param filePath1 Log file to send as attachement
	 * @return
	 */
    public int sendEmail(String emailHost, final String fromEmail, final String passKey, String toEmail1, String toEmail2, String filePath1)
    {
    	ibd.web.Resource.ResourceInitializer.logger.info("HEREEEEEEEEEEEEEEEEEEEEEE");
        final String host = emailHost;
        final String from = fromEmail;
        final String to1 = toEmail1;
        final String to2 = toEmail2;
        final String pass = passKey;
        
        ibd.web.Resource.ResourceInitializer.logger.info("SENDING EMAIL TO: "+to1);
        ibd.web.Resource.ResourceInitializer.logger.info("SENDING EMAIL TO: "+to2);
        ibd.web.Resource.ResourceInitializer.logger.info("SENDING EMAIL FROM: "+from);
        ibd.web.Resource.ResourceInitializer.logger.info("HOST: "+host);

        // Get system properties
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");/*
		
		
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.port", "465");// 25, 465, 587 is supported by OpenShift
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.EnableSSL.enable","true");*/
        // Get session
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });
        
        ibd.web.Resource.ResourceInitializer.logger.info("SESSION STARTED");
        session.setDebug(true);

        // Define message
        MimeMessage message = null;
        MimeBodyPart messageBodyPart1 = null;
        try{
        message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to1));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to2));
        message.setSubject("Log File for: "+new Date());
        ibd.web.Resource.ResourceInitializer.logger.info("FILE TO SEND IS: "+filePath1);
        if(filePath1!=null && !filePath1.trim().equalsIgnoreCase("")){
	        // Handle attachment 1
	        messageBodyPart1 = new MimeBodyPart();
	        messageBodyPart1.attachFile(filePath1);
        }
        }catch(AddressException e){
        	ibd.web.Resource.ResourceInitializer.logger.info("Can not send email due to invalid Address.");
        	return 0;
        }catch(MessagingException e1){
        	ibd.web.Resource.ResourceInitializer.logger.info("Can not send email, either the file is missing or some other problems.");
        	return 0;
        }catch(IOException e2){
        	ibd.web.Resource.ResourceInitializer.logger.info("Can not send email, either the file is missing or some other problems.");
        	return 0;
        }
        // Handle text
        String body = "<html><body>Hello, please find the attached Log for "+new Date()+"...<br/><br/><br/>Regards...<br/>Teedix MarketPredictor</body></html>";
        ibd.web.Resource.ResourceInitializer.logger.info("TEXT ADDED TO EMAIL");
        try{
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
        textPart.setContent(body, "text/html; charset=utf-8");

        MimeMultipart multipart = new MimeMultipart("mixed");

        multipart.addBodyPart(textPart);
        if(filePath1!=null && !filePath1.trim().equalsIgnoreCase("")){
        	multipart.addBodyPart(messageBodyPart1);
        }
        ibd.web.Resource.ResourceInitializer.logger.info("ADDED BODY PART AND ATTACHED FILE");
        message.setContent(multipart);
        // Send message
        ibd.web.Resource.ResourceInitializer.logger.info("STARTED SENDING MESSAGE");
        Transport.send(message);
        ibd.web.Resource.ResourceInitializer.logger.info("FINISHED SENDING MESSAGE");
        ibd.web.Resource.ResourceInitializer.logger.info("MESSAGE SENT");
        }catch(AddressException e){
        	ibd.web.Resource.ResourceInitializer.logger.info("Can not send email due to invalid Address.");
        	return 0;
        }catch(MessagingException e1){
        	ibd.web.Resource.ResourceInitializer.logger.info("Can not send email, either the file is missing or some other problems.");
        	return 0;
        }catch(Exception e2){
        	ibd.web.Resource.ResourceInitializer.logger.info("Can not send email due to some SEVERE error.");
        	return 0;
        }
        return 1;
    }
}