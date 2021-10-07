package com.love2code.springredditbackend.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.love2code.springredditbackend.exception.SpringRedditException;
import com.love2code.springredditbackend.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private final MailContentBuilder mailContentBuilder;
	
	@Async
	void sendMail(NotificationEmail notificationEmail) {
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host","smtp.mailtrap.io");
		properties.put("mail.smtp.port","25");
		
		try {
			Session session = Session.getInstance(properties);
			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			  message.setFrom("springreddit@email.com");
			  message.setRecipients(Message.RecipientType.TO, 
					  InternetAddress.parse(notificationEmail.getRecipient()));
			  message.setSubject(notificationEmail.getSubject(), "UTF-8");
			  message.setContent(mailContentBuilder.build(notificationEmail.getBody()), "text/html");
			 
			  Transport transport = session.getTransport("smtp");
			   transport.connect("227f76393ed452", "dce951cb26f7bb");
			   transport.sendMessage(message, message.getAllRecipients());
			  
			log.info("Activation Mail Sent !!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new SpringRedditException("Exception occured when sending mail to the email id "
					+ notificationEmail.getRecipient());
			
		}
	}

}
