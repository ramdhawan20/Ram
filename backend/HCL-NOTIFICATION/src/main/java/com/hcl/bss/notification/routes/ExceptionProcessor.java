package com.hcl.bss.notification.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ExceptionProcessor  implements Processor{

	public void process(Exchange exchange) throws Exception {
		System.out.println("processing " + exchange.getIn().getBody(String.class));
		exchange.getIn().setBody("Exception occured");
	}

}
