package com.hcl.bss.notification.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.bss.notification.email.sender.EmailNotificationProducer;
import com.hcl.bss.notification.messaging.producer.SmsNotificationProducer;
@Component
public class SubscriptionCamelRouter extends RouteBuilder {
	@Value("${spring.jms.topic}")
	 String topic;
	
	EmailNotificationProducer emailNotificationProducer;
	SmsNotificationProducer smsNotificationProducer;
	public SubscriptionCamelRouter(EmailNotificationProducer emailNotificationProducer,SmsNotificationProducer smsNotificationProducer) {
		super();
		this.emailNotificationProducer = emailNotificationProducer;
		this.smsNotificationProducer = smsNotificationProducer;
	}

  @Override
  public void configure() throws Exception {
	  
	  	  EmailProcessor emailProcessor = new EmailProcessor(emailNotificationProducer);
	  	SmsBean smsBean = new SmsBean(smsNotificationProducer);
	  	onException(Exception.class).handled(true)
	  	.process(new ExceptionProcessor());	
      from("jms:topic:subscriptionr-topic")
      .process(emailProcessor)
      .bean(smsBean);
      
 
	}
}

