package com.hcl.bss.controllers;

import com.hcl.bss.domain.Users;
import com.hcl.bss.dto.Greeting;
import com.hcl.bss.dto.UserDetails;
import com.hcl.bss.repository.UserRepository;
import com.hcl.bss.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController{

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public UserRepository userRepository;


    @ApiOperation(value = "Get user details after successfull login", response = UserDetails.class)
    @RequestMapping(value = "/login",
    produces = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<UserDetails> login(@RequestParam(value = "userId", required = true) String userID, @RequestParam(value = "pwd", required = true) String pwd) {
		UserDetails userDetails = new UserDetails();
		if("ranjan479".equals(userID) && "myPassword".equals(pwd)) {
    	
		userDetails.setUserId(userID);
		userDetails.setUserFirstName("Ranjan");
		userDetails.setUserLastName("Yadav");
		userDetails.setLoggedIn(true);
		}
		else {
			userDetails.setUserId(userID);
			userDetails.setLoggedIn(false);
		}
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
		
       
    }

    @ApiOperation(value = "Get whole list of users", response = UserDetails.class)
    @RequestMapping(value = "/getAllUsers", produces = { "application/json" }, method = RequestMethod.POST)
    public List<UserDetails> getAllUsers() {
  List<UserDetails> userDetailsList = userRepository.isData();
        return userDetailsList;
    	
    }
	
	@ApiOperation(value = "Logout user from current session", response = UserDetails.class)
    @RequestMapping(value = "/logout",
    produces = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<UserDetails> logout(@RequestParam(value = "userId", required = true) String userId) {
		UserDetails userDetails = new UserDetails();
		userDetails.setUserId(userId);
		userDetails.setLoggedIn(false);
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }


	@ApiOperation(value = "Check the scope of current sessionID", response = UserDetails.class)
    @RequestMapping(value = "/isAuthenticated",
    produces = { "application/json" }, method = RequestMethod.GET)
    public boolean isAuthenticated(@RequestParam(value = "sessionID", required = true) String sessionId) {
    	return true;
    }


    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(required=false, defaultValue="World") String name) {
        System.out.println("==== in greeting ====");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @Autowired
    UserServices userServices;

    @RequestMapping(value = "/getUserById",
            produces = { "application/json" }, method = RequestMethod.GET
    )
    public Users findUserById(@RequestParam(required=true, defaultValue="1") int id) {
       Users users = this.userServices.findById(id);
        return users;
    	
    }



}
