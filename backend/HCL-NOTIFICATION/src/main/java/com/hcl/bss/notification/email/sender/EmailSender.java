package com.hcl.bss.notification.email.sender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bss.notification.domain.SubscriptionNotification;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.service.EmailContentBuilder;
import com.hcl.bss.notification.repository.SubscriptionNotificationRepository;
/**
 * 
 * @author ranjankumar.y
 *
 */
@Service
public class EmailSender implements EmailNotificationProducer {
	@Autowired
	private EmailContentBuilder emailContentBuilder;
	@Autowired
	SubscriptionNotificationRepository subscriptionNotificationRepository;
/**
 * 
 */
	@Override
	public void createMail(CustomerDto customer) {
		String htmlContent = emailContentBuilder.buildEmailContent(customer);
		String subscriptionId = customer.getSubscriptionDto().getSubscriptionId();
		String toEmail = customer.getEmailAddress();
		sendMail(htmlContent,subscriptionId,toEmail);
	}

	@Override
	public void sendMail(String htmlContent, String subscriptionId, String toEmail) {
		String to = toEmail;
	  
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", "localhost");
		properties.put("mail.smtp.port", "25");

		
		String from = "subscriptions_hclbss@mail.hcl.com";
		Session session = Session.getDefaultInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Subscription Detail for "+subscriptionId);
			String htmlBody = htmlContent;
			message.setContent(htmlBody, "text/html; charset=utf-8");
			Transport.send(message);
			System.out.println("Sent message successfully....");
			SubscriptionNotification subscriptionNotification  = new SubscriptionNotification();
			SubscriptionNotification subscriptionNotificationDB  = new SubscriptionNotification();
			subscriptionNotificationDB = subscriptionNotificationRepository.findBySubscriptionId(subscriptionId);
			if(null != subscriptionNotificationDB) {
				subscriptionNotificationDB.setEmailStatus('Y');
				subscriptionNotificationDB.setSubscriptionEvent("CreateSubscription");
				subscriptionNotificationRepository.save(subscriptionNotificationDB);
			}else {
			subscriptionNotification.setSubscriptionId(subscriptionId);
			subscriptionNotification.setEmailStatus('Y');
			subscriptionNotification.setSubscriptionEvent("CreateSubscription");
			subscriptionNotificationRepository.save(subscriptionNotification);
			}
		} 
		catch(SendFailedException sfe) {
			System.out.println("Mail not send");
			sfe.printStackTrace();
		}
		catch (Exception ex) {
			System.out.println("Mail not send");
			ex.printStackTrace();
		}


	}

}
