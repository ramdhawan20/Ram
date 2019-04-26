package com.hcl.bss.notification.messaging.producer;

import com.hcl.bss.notification.dto.BatchDto;
import com.hcl.bss.notification.dto.CustomerDto;

public interface SmsNotificationProducer {
	public void createSms(CustomerDto customer);
	public void sendSms(String content);
}
