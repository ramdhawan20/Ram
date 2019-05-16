package com.hcl.bss.notification.email.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.hcl.bss.notification.dto.CustomerDto;
import static com.hcl.bss.notification.constant.NotificationConstant.CUSTOMER;
import static com.hcl.bss.notification.constant.NotificationConstant.CANCELLED;
import static com.hcl.bss.notification.constant.NotificationConstant.BLANK;
import static com.hcl.bss.notification.constant.NotificationConstant.CANCELLATION_HTML;
import static com.hcl.bss.notification.constant.NotificationConstant.SUBSCRIPTION_HTML;


@Service
public class EmailContentBuilder {
	 private TemplateEngine templateEngine;
	 @Autowired
	    public EmailContentBuilder(TemplateEngine templateEngine) {
	        this.templateEngine = templateEngine;
	    }
	 public String buildEmailContent(CustomerDto customer, String status) {
		    
		        Context context = new Context();
		        context.setVariable(CUSTOMER, customer);
		        String output = BLANK;
		        if(CANCELLED.equalsIgnoreCase(status)) {
		        	output = templateEngine.process(CANCELLATION_HTML, context);
		        }
		        else {
		    	output = templateEngine.process(SUBSCRIPTION_HTML, context);
		        }
		    	context = null;
		        return output;
		    }
}
