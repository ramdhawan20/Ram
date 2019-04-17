package com.hcl.bss.notification.routes;

import org.apache.camel.builder.RouteBuilder;

public class CreateTopicRouteBuilder extends RouteBuilder {
	 public static final String ORDER_TOPIC = "jms:topic:order-topic";
	 public static final String ORDER_TOPIC_OUT = "jms:topic:order-topic-out";

    private String FILE_LOCATION = "file:\\PROJECTS\\SUBSCRIPTION\\FILES\\ACTIVEMQ";
    //configure route for jms component
    @Override
    public void configure() throws Exception {
      /*  from(ORDER_TOPIC)
                .process(new NotificationRouter())
        //  .bean(new TransformationDTO());
        .to(ORDER_TOPIC_OUT);*/
    }
}
