package com.hcl.bss.notification.messaging.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bss.notification.domain.SubscriptionNotification;
import com.hcl.bss.notification.repository.SubscriptionNotificationRepository;
@Service
public class SmsSender implements SmsNotificationProducer {
	@Autowired
	SubscriptionNotificationRepository subscriptionNotificationRepository;
	
	@Override
	public void sendSms(String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createSms(String sms, String toPhoneNo, String subscriptionId) {
		// TODO Auto-generated method stub
		System.out.println(sms +" :: " + toPhoneNo);
		// After successfull msg delivered make entry in DB 
		SubscriptionNotification subscriptionNotification  = new SubscriptionNotification();
		subscriptionNotification = subscriptionNotificationRepository.findBySubscriptionId(subscriptionId);
		subscriptionNotification.setSmsStatus('Y');
		subscriptionNotificationRepository.save(subscriptionNotification);
		
	}

}
