package com.hcl.bss.notification.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.hcl.bss.notification.dto.BatchDto;
import com.hcl.bss.notification.dto.CustomerDto;


/**
 * 
 * @author ranjankumar.y
 *
 */
@Service
public class NotificationSender {
	 public static final String Subscription_TOPIC = "jms:topic:subscriptionr-topic";
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 
	 * @param order
	 */
	public void createTopic(CustomerDto customer) {
		System.out.println("sending with convertAndSend() to queue <" + customer + ">");
		jmsTemplate.convertAndSend(Subscription_TOPIC, customer);
	}
}
