package com.hcl.bss.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.Subscription1;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.services.SubscriptionService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
public class SubscriptionController {
	@Autowired
	SubscriptionService subscriptionService;

	@ApiOperation(value = "Get list of all subscription", response = SubscriptionInOut.class)
	//@GetMapping(value = "/subscriptions")
	@PutMapping(value = "/subscriptions")
	public ResponseEntity<?> findAllSubscription(@Valid @RequestBody SubscriptionInOut subscriptionIn) {
	//public ResponseEntity<?> findAllSubscription(@RequestParam(value = "subscriptionId", required = false) String subscriptionId) {
		SubscriptionInOut subscriptionInOut = new SubscriptionInOut();

		try {
			List<Subscription1> subscriptionList = subscriptionService.findAllSubscription(subscriptionIn);
			
			if(subscriptionList != null && subscriptionList.size() > 0) {
				subscriptionInOut.setSuccess(true);
				//subscriptionInOut.setResponseCode(HttpStatus.OK);;
				subscriptionInOut.setMessage("All subscription fetched successfully!");
				subscriptionInOut.setSubscriptionList(subscriptionList);
				
				return new ResponseEntity<>(subscriptionInOut, HttpStatus.OK);
				//return new ResponseEntity<SubscriptionInOut>(subscriptionInOut, HttpStatus.OK);
			}else {
				subscriptionInOut.setSuccess(true);
				//subscriptionInOut.setResponseCode(HttpStatus.NOT_FOUND);
				subscriptionInOut.setMessage("No subscription found!");
				subscriptionInOut.setSubscriptionList(subscriptionList);
				
				return new ResponseEntity<>(subscriptionInOut, HttpStatus.NOT_FOUND);
				//return new ResponseEntity<SubscriptionInOut>(subscriptionInOut, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			subscriptionInOut.setSuccess(false);
			subscriptionInOut.setMessage(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(subscriptionInOut, HttpStatus.INTERNAL_SERVER_ERROR);
			//return new ResponseEntity<SubscriptionInOut>(subscriptionInOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
/*	@ApiOperation(value = "To create new user", response = UserDetails.class)
	@PostMapping(value = "/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody  UserDetails userDetails) {
	public ResponseEntity<?> addUser(@RequestParam(value="userId", required = true) String userId, 
			@RequestParam(value="userFirstName", required = true) String firstName, 
			@RequestParam(value="userMiddleName", required = false) String middleName, 
			@RequestParam(value="userLastName", required = false) String lastName, 
			@RequestParam(value="attribute", required = false) String attribute, 
			@RequestParam(value="userProfile", required = false, defaultValue="1") String userProfile) {
		User user = new User();
		user.setUserId(userDetails.getUserId());
		user.setUserFirstName(userDetails.getUserFirstName());
		user.setUserLastName(userDetails.getUserMiddleName());
		user.setUserMiddleName(userDetails.getUserLastName());
		user.setRoleId(userDetails.getUserProfile().trim().equalsIgnoreCase("Admin") ? 0 : 1);			
		user.setPassword(userDetails.getAttribute());
		
		User createdUser = this.userServices.addUser(user);
		
		return new ResponseEntity<String>("{message: User created successfully}", HttpStatus.OK);
	}

	@ApiOperation(value = "To update existing user", response = UserDetails.class)
	@PutMapping(value = "/user")
	public ResponseEntity<?> editUser(@Valid @RequestBody  UserDetails userDetails) {
		User user = new User();
		user.setUserId(userDetails.getUserId());
		user.setUserFirstName(userDetails.getUserFirstName());
		user.setUserLastName(userDetails.getUserMiddleName());
		user.setUserMiddleName(userDetails.getUserLastName());
		user.setRoleId(userDetails.getUserProfile().trim().equalsIgnoreCase("Admin") ? 0 : 1);			

		User updatedUser = this.userServices.editUser(user);
		
		return new ResponseEntity<>("{message: User updated successfully}", HttpStatus.OK);
	}

	@ApiOperation(value = "To activate/deactivate existing user", response = UserDetails.class)
	@PutMapping(value = "/activate")
	public ResponseEntity<?> activateUser(@Valid @RequestBody  UserDetails userDetails) {
	public ResponseEntity<?> activateUser(@RequestParam(value="userId", required = true) String userId, 
			@RequestParam(value="isActivate", required = true) String isActivate) {
		
		User user = new User();
		user.setUserId(userDetails.getUserId());
		//user.setIsLocked(Integer.parseInt(userDetails.getIsLocked()));

		User updatedUser = this.userServices.activateUser(user);
		
		if(updatedUser.getIsLocked() == 0) {
			return new ResponseEntity<>("{message: User deactivated successfully}", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("{message: User activated successfully}", HttpStatus.OK);
		}
		
	}

	@ApiOperation(value = "To reset user password", response = UserDetails.class)
	@PutMapping(value = "/reset")
	public ResponseEntity<?> resetUser(@Valid @RequestBody  UserDetails userDetails) {
	public ResponseEntity<?> resetUser(@RequestParam(value="userId", required = true) String userId, 
			//@RequestParam(value="oldAttribute", required = true) String oldAttribute,
			@RequestParam(value="newAttribute", required = true) String newAttribute) {
		
		User user = new User();
		user.setUserId(userDetails.getUserId());
		user.setPassword(userDetails.getNewAttribute());

		User updatedUser = this.userServices.resetUser(user);
		
		return new ResponseEntity<>("{message: Password changed successfully}", HttpStatus.OK);
	}
*/
}
