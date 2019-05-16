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
	 public static final String HI="Hi ";
	 public static final String SPACE=" ";
	 public static final String YOUR_SUBSCRIPTION=" your subscription for subscription id : ";
	 public static final String COMPLETED=" completted ";
	 public static final String CANCELLED=" cancelled ";
	
	 
	@Autowired
	SmsNotificationProducer smsNotificationProducer;
	 public SmsBean(SmsNotificationProducer smsNotificationProducer) {
		this.smsNotificationProducer = smsNotificationProducer;
	}
	@Handler
	 public void routeSms(Exchange exchange) throws JsonParseException, JsonMappingException, IOException {
		String sms = "";
		 ObjectMapper mapper = new ObjectMapper();
			String str = exchange.getIn().getBody(String.class);
			CustomerDto customer = mapper.readValue(str, CustomerDto.class);
			String toPhoneNo = customer.getPhoneNo();
			String subscriptionId = customer.getSubscriptionDto().getSubscriptionId();
			String status = customer.getSubscriptionDto().getStatus();
			if("CANCELLED".equalsIgnoreCase(customer.getSubscriptionDto().getStatus())) {
				sms = HI +customer.getFirstName()+ SPACE + customer.getLastName()+ YOUR_SUBSCRIPTION + subscriptionId+ CANCELLED;
			}else {
			sms = HI +customer.getFirstName()+ SPACE + customer.getLastName()+ YOUR_SUBSCRIPTION + subscriptionId+ COMPLETED;
			}
		 smsNotificationProducer.createSms(sms, toPhoneNo, subscriptionId, status);
		 
	 }
}
