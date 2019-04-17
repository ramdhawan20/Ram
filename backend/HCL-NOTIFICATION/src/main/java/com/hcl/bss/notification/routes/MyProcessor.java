package com.hcl.bss.notification.routes;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.sender.EmailNotificationProducer;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;
@Service
public class MyProcessor implements Processor {
	EmailNotificationProducer emailNotificationProducer;
	public MyProcessor(EmailNotificationProducer emailNotificationProducer) {
		super();
		this.emailNotificationProducer = emailNotificationProducer;
	}
/*
	@Autowired
	EmailNotificationProducer emailNotificationProducer;
	public void setEmailNotificationProducer(EmailNotificationProducer emailNotificationProducer) {
		this.emailNotificationProducer = emailNotificationProducer;
	}
	
	@Autowired
	SmsNotificationProducer smsNotificationProducer;
	public void setSmsNotificationProducer(SmsNotificationProducer smsNotificationProducer) {
		this.smsNotificationProducer = smsNotificationProducer;
	}*/
	
	@Override
	public void process(Exchange exchange) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String str = exchange.getIn().getBody(String.class);
		CustomerDto customer = mapper.readValue(str, CustomerDto.class);

					 System.out.println("##################I###############" +emailNotificationProducer);
					 System.out.println(customer);
					 emailNotificationProducer.createMail(customer);
				      // smsNotificationProducer.createSms(customer);
				}
	}


