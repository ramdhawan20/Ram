package com.hcl.bss.schedulers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.dto.CustomerDto;
import com.hcl.bss.services.SubscriptionService;


/*
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
*/

/**
 *
 * author ranjankumar.y
 *
 * Description
 * Core responsiblity of this scheduler is to send notificaiton
 * whether its SMS / Email.
 * Scheduler will populate the records in EMAIL_NOTIFICATION_STATUS and SMS_NOTIFICATION_STATUS
 * tables.
 *
 */
@Component
public class NotificationScheduler {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	SubscriptionService subscriptionService;
	public static final String SUBSCRIPTION_NOTIFICATION_URL = "http://localhost:8081/notifySubscriptionDetail?subscriptionId=";
	public static final String SUBSCRIPTION_NOTIFICATION_CREATED = "&eventType=CreatedSubscription";
	public static final String SUBSCRIPTION_NOTIFICATION_CANCELLED = "&eventType=CancelledSubscription";

	 @Scheduled(cron="0 0 0 * * ?")
	    public void runSubscriptionDetails() throws Exception{
		 List<String> subIds = subscriptionService.getLastSubscriptionIds();
		 List<String> canceledsubIds = subscriptionService.getLastCanceledSubscriptionIds();
		for(String subId :subIds) {
			restTemplate.getForObject(SUBSCRIPTION_NOTIFICATION_URL+subId+SUBSCRIPTION_NOTIFICATION_CREATED, CustomerDto.class);
		} 
		for(String subId :canceledsubIds) {
			restTemplate.getForObject(SUBSCRIPTION_NOTIFICATION_URL+subId+SUBSCRIPTION_NOTIFICATION_CANCELLED, CustomerDto.class);
		} 
	    }
	 
	}

