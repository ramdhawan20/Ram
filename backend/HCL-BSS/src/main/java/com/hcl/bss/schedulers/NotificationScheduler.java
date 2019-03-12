package com.hcl.bss.schedulers;


/*
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
*/

/**
 *
 * author dhiraj.s
 *
 * Description
 * Core responsiblity of this scheduler is to send notificaiton
 * whether its SMS / Email.
 * Scheduler will populate the records in EMAIL_NOTIFICATION_STATUS and SMS_NOTIFICATION_STATUS
 * tables.
 *
 */
public class NotificationScheduler {

   /* public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        try {
            context.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("activemq:queue:test.queue")
                            .to("stream:out");
                }
            });
            ProducerTemplate template = context.createProducerTemplate();
            context.start();
            template.sendBody("activemq:test.queue", "Hello World");
            Thread.sleep(2000);
        } finally {
            context.stop();
        }
    }
*/
}
