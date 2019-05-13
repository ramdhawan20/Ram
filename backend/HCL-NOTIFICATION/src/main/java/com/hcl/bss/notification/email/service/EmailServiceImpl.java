package com.hcl.bss.notification.email.service;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.bss.notification.client.BSSClient;
import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.sender.EmailNotificationProducer;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;
import com.hcl.bss.notification.routes.NotificationSender;
import com.hcl.bss.notification.routes.SubscriptionCamelRouter;

@Service
public class EmailServiceImpl implements EmailService {
	@Autowired
	BSSClient bSSClient;
	@Autowired
	NotificationSender notificationSender;
	private TemplateEngine templateEngine;

	@Autowired
	public EmailServiceImpl(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Autowired
	EmailNotificationProducer emailNotificationProducer;
	@Autowired
	SmsNotificationProducer smsNotificationProducer;
	@Value("${spring.activemq.connector}")
	 String connector;
	@Value("${spring.jms.topic}")
	 String topic;


	@Override
	public void emailSubscriptionDetail(String subId) {

		CustomerDto cus = bSSClient.getSubscriptionDetails(subId);
		// without camel routing
		// notificationSender.createTopic(cus);

		// camel routing work and further process

		SubscriptionCamelRouter routeBuilder = new SubscriptionCamelRouter(emailNotificationProducer,smsNotificationProducer);
		CamelContext ctx = new DefaultCamelContext();

		// configure jms component
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(connector);
		ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		try {
			ctx.addRoutes(routeBuilder);
			ProducerTemplate template = ctx.createProducerTemplate();
			ctx.start();
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(cus);
			template.sendBody(topic, jsonString);
			// Thread.sleep(5 * 60 * 1000);
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
