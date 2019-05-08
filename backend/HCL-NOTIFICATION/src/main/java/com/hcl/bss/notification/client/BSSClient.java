package com.hcl.bss.notification.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.notification.dto.CustomerDto;

@Component
public class BSSClient {
	@Value("${url.subscriptionDetail}")
	 String url;
	 public static final String Subscription_TOPIC = "jms:topic:subscriptionr-topic";
	@Autowired
    RestTemplate restTemplate;

	public CustomerDto getSubscriptionDetails(String subId) {
		CustomerDto customer =  restTemplate.getForObject(url+subId, CustomerDto.class);
		return customer;
		
	}
}
