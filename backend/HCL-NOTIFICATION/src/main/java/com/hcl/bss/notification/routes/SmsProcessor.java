package com.hcl.bss.notification.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;
import com.hcl.bss.notification.messaging.producer.SmsSender;

public class SmsProcessor implements Processor{
	SmsNotificationProducer smsNotificationProducer;
	public SmsProcessor(SmsNotificationProducer smsNotificationProducer) {
		super();
		this.smsNotificationProducer = smsNotificationProducer;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		SmsNotificationProducer smsNotificationProducer = new SmsSender();
		ObjectMapper mapper = new ObjectMapper();
		String str = exchange.getIn().getBody(String.class);
		CustomerDto customer = mapper.readValue(str, CustomerDto.class);
		String toPhoneNo = customer.getPhoneNo();
		String subscriptionId = customer.getSubscriptionDto().getSubscriptionId();
		String sms = "Hi " +customer.getFirstName()+ " " + customer.getLastName()+ ", Your Subscription with Subcription Id : " + subscriptionId+ " is completed";
		// in create sms third party smsgate will be integrated 
		smsNotificationProducer.createSms(sms,toPhoneNo,subscriptionId);
		
	}

}
