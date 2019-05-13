package com.hcl.bss.notification.messaging.producer;

public interface SmsNotificationProducer {
	public void sendSms(String content);
	public void createSms(String sms, String toPhoneNo, String subscriptionId);
}
