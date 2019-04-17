package com.hcl.bss.notification.email.sender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.service.EmailContentBuilder;
/**
 * 
 * @author ranjankumar.y
 *
 */
@Service
public class EmailSender implements EmailNotificationProducer {
	@Autowired
	private EmailContentBuilder emailContentBuilder;
/**
 * 
 */
	@Override
	public void createMail(CustomerDto customer) {
		String htmlContent = emailContentBuilder.buildEmailContent(customer);
		System.out.println("<------------------------------------------------------------->");
		System.out.println(htmlContent);
		System.out.println("<------------------------------------------------------------->");
		sendMail(htmlContent);
	}

	@Override
	public void sendMail(String htmlContent) {
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", "localhost");
		properties.put("mail.smtp.port", "25");

		String to = "ranjankumar.y@hcl.com";
		String from = "orders_hclbss@mail.hcl.com";
		Session session = Session.getDefaultInstance(properties);
		System.out.println("Preparing to send email....");
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Subscription Detail");
			String htmlBody = htmlContent;
			message.setContent(htmlBody, "text/html; charset=utf-8");
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}

}
