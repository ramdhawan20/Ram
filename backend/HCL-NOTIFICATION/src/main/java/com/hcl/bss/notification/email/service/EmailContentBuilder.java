package com.hcl.bss.notification.email.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.hcl.bss.notification.dto.BatchDto;
import com.hcl.bss.notification.dto.CustomerDto;


@Service
public class EmailContentBuilder {
	 private TemplateEngine templateEngine;
	 @Autowired
	    public EmailContentBuilder(TemplateEngine templateEngine) {
	        this.templateEngine = templateEngine;
	    }
	 public String buildEmailContent(CustomerDto customer) {
		    
		        Context context = new Context();
		        context.setVariable("customer", customer);
		        String output = "";
		        
		    	output = templateEngine.process("subscription_mail_notification", context);
		    	
		    	context = null;
		        return output;
		    }
}
