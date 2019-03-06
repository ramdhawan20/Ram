package com.hcl.bss.controllers;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.User;
import com.hcl.bss.dto.Greeting;
import com.hcl.bss.dto.LoginRequest;
import com.hcl.bss.dto.UserDetails;
import com.hcl.bss.exceptions.AuthenticationException;
import com.hcl.bss.repository.UserRepository;
import com.hcl.bss.services.UserServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@CrossOrigin(origins = "*")
@RestController
public class AuthenticationController{

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public UserRepository userRepository;


	@ApiOperation(value = "Get user details after successfull login", response = UserDetails.class)
    @RequestMapping(value = "/login", produces = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<UserDetails> login(@RequestBody LoginRequest loginRequest) {

	     User user = userRepository.isUserExists(loginRequest.getUserId(),loginRequest.getPassword());
	     if (user == null) {
             throw new AuthenticationException("Invalid User Id / Pwd");
         }else if (user.getIsLocked() == 1) {
             throw new AuthenticationException("User is Locked. Please contact System Admin !!");
        }

    	UserDetails userDetails = new UserDetails();
        userDetails.setUserId(user.getUserId());
		userDetails.setUserFirstName(user.getUserFirstName());
        userDetails.setUserLastName(user.getUserLastName());
        return new ResponseEntity<>(userDetails, HttpStatus.OK);

    }

    /*@ApiOperation(value = "Get whole list of users", response = UserDetails.class)
    @RequestMapping(value = "/getAllUsers", produces = { "application/json" }, method = RequestMethod.POST)
    public List<UserDetails> getAllUsers() {
        List<UserDetails> userDetailsList = userRepository.isData();
        return userDetailsList;
    }*/

	@ApiOperation(value = "Logout user from current session", response = UserDetails.class)
    @RequestMapping(value = "/logout",
    produces = { "application/json" }, method = RequestMethod.POST)
    public boolean logout(@RequestParam(value = "sessionID", required = true) String sessionId) {
    	return true;
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
    public User findUserById(@RequestParam(required=true, defaultValue="1") int id) {
        User user = this.userServices.findById(id);
        return user;
    }



}
