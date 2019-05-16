package com.hcl.bss.notification.client;

import static com.hcl.bss.notification.constant.NotificationConstant.CANCELLED_SUBSCRIPTION;
import static com.hcl.bss.notification.constant.NotificationConstant.CREATED_SUBSCRIPTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.notification.dto.CustomerDto;

@Component
public class BSSClient {
	@Value("${url.subscriptionDetail}")
	 String url;
	@Value("${url.canceledSubscriptionDetail}")
	 String cancelUrl;
	
	 public static final String Subscription_TOPIC = "jms:topic:subscriptionr-topic";
	@Autowired
    RestTemplate restTemplate;
	
	public CustomerDto getSubscriptionDetails(String subId,String eventType) {
		CustomerDto customer = new CustomerDto();
		if(CREATED_SUBSCRIPTION.equalsIgnoreCase(eventType)) {
		customer =  restTemplate.getForObject(url+subId, CustomerDto.class);
		}
		else if(CANCELLED_SUBSCRIPTION.equalsIgnoreCase(eventType)) {
			customer = restTemplate.getForObject(cancelUrl+subId, CustomerDto.class);	
		}
		return customer;
		
	}
	
	
}
