package com.hcl.bss.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.notification.dto.CustomerDto;
import com.hcl.bss.notification.email.service.EmailService;
import com.hcl.bss.notification.repository.SubscriptionRepository;

import io.swagger.annotations.ApiOperation;

/**
 * @author ranjankumar.y
 *
 */
@CrossOrigin(origins = "*")
@RestController
public class NotificationController {
	@Autowired
	EmailService emailService;
	@Autowired
	SubscriptionRepository subscriptionRepository;
	@ApiOperation(value="Get Details of a Subscription", response= CustomerDto.class)
	@RequestMapping(value="/notifySubscriptionDetail", produces = { "application/json" }, method = RequestMethod.GET)
	
	public void emailSubscriptionDetail(@RequestParam(value = "subscriptionId", required = true) String subId,
			@RequestParam(value = "eventType", required = true) String eventType) throws Exception{
	
		try {
		emailService.emailSubscriptionDetail(subId,eventType);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
