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
    	CustomErrorResponse errors = new CustomErrorResponse();
         errors.setTimestamp(LocalDateTime.now());
         errors.setError(ex.getMessage());
         errors.setStatus(HttpStatus.NOT_FOUND.value());

         return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
    
    //... Exception for User Management
    @ExceptionHandler(CustomUserMgmtException.class)
	public ResponseEntity<CustomErrorResponse> subExceptionHandler(CustomUserMgmtException ex) {
    	CustomErrorResponse error = new CustomErrorResponse();
             if(ex.getCode().equals(100)) {
             	error.setTimestamp(LocalDateTime.now());   
            	error.setStatus(HttpStatus.NOT_FOUND.value()); 	 
             	error.setError("No User Found");
             } 
             else if(ex.getCode().equals(101)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("Menu not found for this user");
             }
             else if(ex.getCode().equals(103)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("Role is not associated with this user");
             }
             else if(ex.getCode().equals(104)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("No profile found");
             }
             else if(ex.getCode().equals(105)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.NOT_FOUND.value());
            	 error.setError("Profile creation failed");
             }
             else if(ex.getCode().equals(106)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.BAD_REQUEST.value());
            	 error.setError("Profile mapping failed");
             }
             else if(ex.getCode().equals(107)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.BAD_REQUEST.value());
            	 error.setError("Role Name not found");
             }
             else if(ex.getCode().equals(108)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.BAD_REQUEST.value());
            	 error.setError("Menu Name not found");
             }
             else if(ex.getCode().equals(109)) {
            	 error.setTimestamp(LocalDateTime.now());
            	 error.setStatus(HttpStatus.BAD_REQUEST.value());
            	 error.setError("Sub Menu Name not found");
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

