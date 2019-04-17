package com.hcl.bss.notification.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bss.notification.client.BSSClient;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.routes.NotificationSender;
@Service
public class EmailServiceImpl implements EmailService{
@Autowired
BSSClient bSSClient;
@Autowired
NotificationSender notificationSender;
	@Override
	public void emailSubscriptionDetail(String subId) {
		
		//without camel routing
		CustomerDto cus = bSSClient.getSubscriptionDetails(subId);
		notificationSender.createTopic(cus);
		
		
		
		
		// camel routing work and further process
	
	/* SampleCamelRouter routeBuilder = new SampleCamelRouter();
     //CreateTopicRouteBuilder createTopicRouteBuilder = new CreateTopicRouteBuilder();
     CamelContext ctx = new DefaultCamelContext();

     //configure jms component
     ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
     ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

     try {
         ctx.addRoutes(routeBuilder);
         ProducerTemplate template = ctx.createProducerTemplate();
         ctx.start();
         ObjectMapper mapper = new ObjectMapper();
         String jsonString = mapper.writeValueAsString(cus);
         template.sendBody("jms:topic:subscriptionr-topic",jsonString);
         Thread.sleep(5 * 60 * 1000);
         ctx.stop();
     }
     catch (Exception e) {
         e.printStackTrace();
     }*/
	}
}
