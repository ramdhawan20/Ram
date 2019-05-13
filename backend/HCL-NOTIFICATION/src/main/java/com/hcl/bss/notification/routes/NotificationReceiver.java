package com.hcl.bss.notification.routes;


import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.sender.EmailNotificationProducer;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;

@Component
public class NotificationReceiver {
	 
	 public static final String Subscription_TOPIC = "jms:topic:subscriptionr-topic";
	@Autowired
	EmailNotificationProducer emailNotificationProducer;
	@Autowired
	SmsNotificationProducer smsNotificationProducer;

    @JmsListener(destination = Subscription_TOPIC, containerFactory = "topicListenerFactory")
    public void receiveTopicMessage(@Payload CustomerDto customer,
                                    @Headers MessageHeaders headers,
                                    @SuppressWarnings("rawtypes") Message message,
                                    Session session) {

       System.out.println("received <" + customer + ">");
       
       emailNotificationProducer.createMail(customer);
      // smsNotificationProducer.createSms(customer);
    }

	
		
	}

