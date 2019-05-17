package com.hcl.bss.notification.routes;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;
import static com.hcl.bss.notification.constant.NotificationConstant.BLANK;
import static com.hcl.bss.notification.constant.NotificationConstant.CANCELLED;
import static com.hcl.bss.notification.constant.NotificationConstant.HI;
import static com.hcl.bss.notification.constant.NotificationConstant.SPACE;
import static com.hcl.bss.notification.constant.NotificationConstant.YOUR_SUBSCRIPTION;
import static com.hcl.bss.notification.constant.NotificationConstant.COMPLETED;
import static com.hcl.bss.notification.constant.NotificationConstant.IS_CANCELLED;

public class SmsBean {

	
	 
	@Autowired
	SmsNotificationProducer smsNotificationProducer;
	 public SmsBean(SmsNotificationProducer smsNotificationProducer) {
		this.smsNotificationProducer = smsNotificationProducer;
	}
	@Handler
	 public void routeSms(Exchange exchange) throws JsonParseException, JsonMappingException, IOException {
		String sms = BLANK;
		 ObjectMapper mapper = new ObjectMapper();
			String str = exchange.getIn().getBody(String.class);
			CustomerDto customer = mapper.readValue(str, CustomerDto.class);
			String toPhoneNo = customer.getPhoneNo();
			String subscriptionId = customer.getSubscriptionDto().getSubscriptionId();
			String status = customer.getSubscriptionDto().getStatus();
			if(CANCELLED.equalsIgnoreCase(customer.getSubscriptionDto().getStatus())) {
				sms = HI +customer.getFirstName()+ SPACE + customer.getLastName()+ YOUR_SUBSCRIPTION + subscriptionId+ IS_CANCELLED;
			}else {
			sms = HI +customer.getFirstName()+ SPACE + customer.getLastName()+ YOUR_SUBSCRIPTION + subscriptionId+ COMPLETED;
			}
		 smsNotificationProducer.createSms(sms, toPhoneNo, subscriptionId, status);
		 
	 }
}
