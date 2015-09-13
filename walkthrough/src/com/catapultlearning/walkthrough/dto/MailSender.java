package com.catapultlearning.walkthrough.dto;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailSender {

private Properties props = System.getProperties();
	
	private MailAuthenticator authenticator;
	
	private Session session;
	
	public MailSender(){}
	
	public MailSender(String username,String password,String smtpHostName){
		init(username,password,smtpHostName);
	}
	
	public void init(String username,String password,String smtpHostName){
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		authenticator = new MailAuthenticator(username,password);
		
		// Create session
		session = Session.getInstance(props, authenticator);
	}
	
	public void send(String recipient,String subject,Object content){
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(authenticator.getUsername()));
			message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
			
			message.setSubject(subject);
			message.setContent(content.toString(), "text/html");
			
			Transport.send(message);
		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}
}
