package com.hcl.bss.notification.routes;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.sender.EmailNotificationProducer;

public class MyBean {
	@Autowired
	EmailNotificationProducer emailNotificationProducer;
	 @Handler
	 public void doSomething(Exchange exchange) throws JsonParseException, JsonMappingException, IOException {
		 ObjectMapper mapper = new ObjectMapper();
			String str = exchange.getIn().getBody(String.class);
			CustomerDto customer = mapper.readValue(str, CustomerDto.class);
		// emailNotificationProducer.createMail(customer);
		 
	 }
}
