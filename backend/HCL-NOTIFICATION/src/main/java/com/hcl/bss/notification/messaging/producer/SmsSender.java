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
	public void createSms(String sms, String toPhoneNo, String subscriptionId, String status) {
		
		System.out.println(sms +" :: " + toPhoneNo);
		
		
		if("CANCELLED".equalsIgnoreCase(status)) {
			SubscriptionNotification subscriptionNotification  = new SubscriptionNotification();
			SubscriptionNotification subscriptionNotificationDB  = new SubscriptionNotification();
			subscriptionNotificationDB = subscriptionNotificationRepository.findBySubscriptionId(subscriptionId);
			if(null != subscriptionNotificationDB) {
				subscriptionNotificationDB.setSmsStatus('Y');
				subscriptionNotificationDB.setCancelledEvent('Y');
				subscriptionNotificationRepository.save(subscriptionNotificationDB);
			}else {
			subscriptionNotification.setSubscriptionId(subscriptionId);
			subscriptionNotification.setEmailStatus('Y');
			subscriptionNotification.setCancelledEvent('Y');
			subscriptionNotificationRepository.save(subscriptionNotification);
		}
		}
		else {
			SubscriptionNotification subscriptionNotification  = new SubscriptionNotification();
			SubscriptionNotification subscriptionNotificationDB  = new SubscriptionNotification();
		
		subscriptionNotificationDB = subscriptionNotificationRepository.findBySubscriptionId(subscriptionId);
		if(null != subscriptionNotificationDB) {
			subscriptionNotificationDB.setSmsStatus('Y');
			subscriptionNotificationDB.setCreateEvent('Y');
			subscriptionNotificationRepository.save(subscriptionNotificationDB);
		}
		else {
		subscriptionNotification.setSubscriptionId(subscriptionId);
		subscriptionNotification.setSmsStatus('Y');
		subscriptionNotification.setCreateEvent('Y');
		subscriptionNotificationRepository.save(subscriptionNotification);}
		
		}
	}

}
