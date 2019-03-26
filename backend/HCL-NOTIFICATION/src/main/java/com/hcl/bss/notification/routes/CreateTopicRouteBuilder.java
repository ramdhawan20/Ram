package com.hcl.bss.notification.routes;

import com.hcl.bss.notification.process.LoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

public class CreateTopicRouteBuilder extends RouteBuilder {

    private String FILE_LOCATION = "file:\\PROJECTS\\SUBSCRIPTION\\FILES\\ACTIVEMQ";
    //configure route for jms component
    @Override
    public void configure() throws Exception {
        from(FILE_LOCATION).split().tokenize("\n")
                .process(new LoggingProcessor())
        //  .bean(new TransformationDTO());
        .to("jms:topic:hclbss-subscriptions");
    }
}
