package com.hcl.bss.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

/**
 *
 * Author: dhiraj.s
 */

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }
    
    
    //... Exception for subscriptions
    @ExceptionHandler(CustomSubscriptionException.class)
	public ResponseEntity<CustomErrorResponse> subExceptionHandler(CustomSubscriptionException ex) {
    	CustomErrorResponse error = new CustomErrorResponse();
             if(ex.getCode().equals(100)) {
             	error.setTimestamp(LocalDateTime.now());   
            	error.setStatus(HttpStatus.NOT_FOUND.value()); 	 
             	error.setError("No Subscription Found");
             } 
             else if(ex.getCode().equals(101)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("Customer not found");
             }
             else if(ex.getCode().equals(103)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("No Product/RatePlan associated with subscription");
             }
             else if(ex.getCode().equals(104)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("RatePlan not found for one of the product");
             }
             else {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            	 error.setError("Internal Server Error Occurred");
            	 return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
             }
		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.NOT_FOUND);
	}
    
    //...
}

