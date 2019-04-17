package com.hcl.bss.notification.messaging.producer;

import org.springframework.stereotype.Service;

import com.hcl.bss.notification.dto.BatchDto;
import com.hcl.bss.notification.dto.CustomerDto;
@Service
public class SmsSender implements SmsNotificationProducer {

	@Override
	public void createSms(CustomerDto customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSms(String content) {
		// TODO Auto-generated method stub

	}

}
