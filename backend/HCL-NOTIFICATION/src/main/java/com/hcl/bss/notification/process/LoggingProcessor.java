package com.hcl.bss.notification.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LoggingProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println("Received Order: " +
                exchange.getIn().getBody(String.class));
    }
}
