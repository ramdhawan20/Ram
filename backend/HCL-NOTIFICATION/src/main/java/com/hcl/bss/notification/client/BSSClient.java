package com.hcl.bss.notification.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.notification.domain.Subscription;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.dto.SubscriptionDetailDto;

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
		if("CreatedSubscription".equalsIgnoreCase(eventType)) {
		customer =  restTemplate.getForObject(url+subId, CustomerDto.class);
		
		}
		else if("CancelledSubscription".equalsIgnoreCase(eventType)) {
			Subscription subscription = new Subscription();
			customer = restTemplate.getForObject(cancelUrl+subId, CustomerDto.class);
			//customer.setSubscriptionDto(convertEntityTODto(subscription));
			
		}
		return customer;
		
	}
	
	private SubscriptionDetailDto convertEntityTODto(Subscription subscription) {
		SubscriptionDetailDto subscriptionDetailDto = new SubscriptionDetailDto();
		subscriptionDetailDto.setSubscriptionId(subscription.getSubscriptionId());
		return subscriptionDetailDto;
	}

}
