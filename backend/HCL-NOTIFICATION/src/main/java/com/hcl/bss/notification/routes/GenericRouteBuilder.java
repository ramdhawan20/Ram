package com.hcl.bss.notification.routes;

import com.hcl.bss.notification.process.LoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

public class GenericRouteBuilder extends RouteBuilder {

    //configure route for jms component
    @Override
    public void configure() throws Exception {
        //from("file:\\PROJECTS\\SUBSCRIPTION\\FILES\\ACTIVEMQ").split().tokenize("\n")
            from("jms:topic:hclbss-subscriptions")
                .process(new LoggingProcessor());
              //  .bean(new TransformationDTO());
                //.to("jms:queue:javainuse");
    }


}
