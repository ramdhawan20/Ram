package com.hcl.bss.notification.routes;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;

public class SmsBean {
	@Value("${sms.hi}")
	 String hi;
	@Value("${sms.subscription}")
	 String subscription;
	@Value("${sms.completed}")
	 String completed;
	@Autowired
	SmsNotificationProducer smsNotificationProducer;
	 public SmsBean(SmsNotificationProducer smsNotificationProducer) {
		this.smsNotificationProducer = smsNotificationProducer;
	}
	@Handler
	 public void routeSms(Exchange exchange) throws JsonParseException, JsonMappingException, IOException {
		 ObjectMapper mapper = new ObjectMapper();
			String str = exchange.getIn().getBody(String.class);
			CustomerDto customer = mapper.readValue(str, CustomerDto.class);
			String toPhoneNo = customer.getPhoneNo();
			String subscriptionId = customer.getSubscriptionDto().getSubscriptionId();
			String sms = hi +customer.getFirstName()+ " " + customer.getLastName()+ subscription + subscriptionId+ completed;
		 smsNotificationProducer.createSms(sms, toPhoneNo, subscriptionId);
		 
	 }
}
