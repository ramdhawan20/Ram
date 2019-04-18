package com.hcl.bss.notification.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.notification.dto.CustomerDto;

@Component
public class BSSClient {
	public static final String SUBSCRIPTION_DETAILS_URL = "http://localhost:8080//subscriptionDetail?subscriptionId=";
	 public static final String Subscription_TOPIC = "jms:topic:subscriptionr-topic";
	@Autowired
    RestTemplate restTemplate;

	public CustomerDto getSubscriptionDetails(String subId) {
		CustomerDto customer =  restTemplate.getForObject(SUBSCRIPTION_DETAILS_URL+subId, CustomerDto.class);
		return customer;
		
	}
}
