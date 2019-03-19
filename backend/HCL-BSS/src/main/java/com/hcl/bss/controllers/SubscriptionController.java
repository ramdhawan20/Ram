package com.hcl.bss.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.dto.SubscriptionDto;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.services.SubscriptionService;

import io.swagger.annotations.ApiOperation;
/**
 * This is SubscriptionController will handle calls related to subscriptions
 *
 * @author- Vinay Panwar
 */
@CrossOrigin(origins = "*")
@RestController
@PropertySource("classpath:application.properties")
public class SubscriptionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionController.class);
	@Autowired
	SubscriptionService subscriptionService;
	@Value("${app.page.size}") String pageSize;
	
	@ApiOperation(value = "Get list of all subscription", response = SubscriptionInOut.class)
	@PutMapping(value = "/subscriptions")
	public ResponseEntity<?> findAllSubscription(@Valid @RequestBody SubscriptionInOut subscriptionIn) {
	//public ResponseEntity<?> findAllSubscription(@Valid @RequestBody SubscriptionInOut subscriptionIn, Pageable pageable1) {
		LOGGER.info("<-----------------------Start findAllSubscription() method-------------------------------->");
		LOGGER.info("Optional I/P details: " + subscriptionIn.toString());
		
		SubscriptionInOut subscriptionOut = new SubscriptionInOut();
		
		//Pageable pageable = new PageRequest(subscriptionIn.getPageNo(), 1, Sort.Direction.DESC, "createdDate");
		Pageable pageable = new PageRequest(subscriptionIn.getPageNo(), Integer.parseInt(pageSize), Sort.Direction.DESC, "createdDate");

		try {
			if((subscriptionIn.getFromDateStr()!= null && subscriptionIn.getToDateStr()== null) || 
					(subscriptionIn.getToDateStr()!= null && subscriptionIn.getFromDateStr()== null) || 
					(!"".equalsIgnoreCase(subscriptionIn.getFromDateStr()) && "".equalsIgnoreCase(subscriptionIn.getToDateStr())) ||
					(!"".equalsIgnoreCase(subscriptionIn.getToDateStr()) && "".equalsIgnoreCase(subscriptionIn.getFromDateStr())) ) {
				subscriptionOut.setSuccess(false);
				subscriptionOut.setMessage("If fromDate is entered then toDate is mandatory or vice-versa");
				subscriptionOut.setResponseCode(HttpStatus.BAD_REQUEST.value());

				return new ResponseEntity<>(subscriptionOut, HttpStatus.BAD_REQUEST);
			}

			List<SubscriptionDto> subscriptionList = subscriptionService.findAllSubscription(subscriptionIn, pageable);
			
			if(subscriptionList != null && subscriptionList.size() > 0) {
				subscriptionOut.setSuccess(true);
				subscriptionOut.setResponseCode(HttpStatus.OK.value());
				subscriptionOut.setMessage("All subscription fetched successfully!");
				subscriptionOut.setSubscriptionList(subscriptionList);
				subscriptionOut.setTotalPages(subscriptionIn.getTotalPages());
				subscriptionOut.setLastPage(subscriptionIn.isLastPage());
				
				return new ResponseEntity<>(subscriptionOut, HttpStatus.OK);
			}else {
				subscriptionOut.setSuccess(true);
				subscriptionOut.setResponseCode(HttpStatus.NOT_FOUND.value());
				subscriptionOut.setMessage("No subscription found!");
				LOGGER.info("<-----------------------End findAllSubscription() method-------------------------------->");
				return new ResponseEntity<>(subscriptionOut, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			subscriptionOut.setSuccess(false);
			subscriptionOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			return new ResponseEntity<>(subscriptionOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
