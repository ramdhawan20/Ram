package com.hcl.bss.notification.messaging.producer;

public interface SmsNotificationProducer {
	
	public void createSms(String sms, String toPhoneNo, String subscriptionId, String status);
}
