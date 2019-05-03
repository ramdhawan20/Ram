package com.hcl.bss.notification.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.bss.notification.email.sender.EmailNotificationProducer;
@Component
public class SampleCamelRouter extends RouteBuilder {
	/*@Autowired
	  EmailNotificationProducer emailNotificationProducer;*/
	EmailNotificationProducer emailNotificationProducer;
	public SampleCamelRouter(EmailNotificationProducer emailNotificationProducer) {
		super();
		this.emailNotificationProducer = emailNotificationProducer;
	}

  @Override
  public void configure() throws Exception {
	  	  MyProcessor processor = new MyProcessor(emailNotificationProducer);
	  
/*    restConfiguration()
      .component("servlet")
      .bindingMode(RestBindingMode.json);

    rest().get("http://localhost:8080//subscriptionDetail?subscriptionId=SMB02R00232019100002")*/
	  from("jms:topic:subscriptionr-topic")
      .to("jms:topic:subscriptionr-topic");
      from("jms:topic:subscriptionr-topic")
     // .bean(emailNotificationProducer)
     .process(processor);
 
	}
}

