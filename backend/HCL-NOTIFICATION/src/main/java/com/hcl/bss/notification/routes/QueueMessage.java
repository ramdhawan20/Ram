package com.hcl.bss.notification.routes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

/**
 * This scheduler will be responsible to populate the content in JMS Queue
 * this will be in the listening mode and send the content as soon as topic
 * is getting populated
 *
 * author: dhiraj.s
 *
 * TODO: Should be changed to spring scheduler
 */
public class QueueMessage {
    public static void main(String args[]){

       GenericRouteBuilder routeBuilder = new GenericRouteBuilder();
        //CreateTopicRouteBuilder createTopicRouteBuilder = new CreateTopicRouteBuilder();
        CamelContext ctx = new DefaultCamelContext();

        //configure jms component
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        try {
            ctx.addRoutes(routeBuilder);
            ctx.start();
            Thread.sleep(5 * 60 * 1000);
            ctx.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
