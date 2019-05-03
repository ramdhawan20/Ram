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
//    @ExceptionHandler(CustomSubscriptionException.class)
//	public ResponseEntity<CustomErrorResponse> subExceptionHandler(CustomSubscriptionException ex) {
//    	CustomErrorResponse errors = new CustomErrorResponse();
//         errors.setTimestamp(LocalDateTime.now());
//         errors.setError(ex.getMessage());
//         errors.setStatus(HttpStatus.NOT_FOUND.value());
//
//         return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
//    }
    
    //... Exception for User Management
/*    @ExceptionHandler(CustomGlobalException.class)
	public ResponseEntity<CustomErrorResponse> subExceptionHandler(CustomGlobalException ex) {
    	CustomErrorResponse error = new CustomErrorResponse();
		error.setTimestamp(LocalDateTime.now());   
		error.setStatus(ex.getCode()); 	 
		error.setError(ex.getMessage());
		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.NOT_FOUND);
	}
*/
  //...
    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<CustomErrorResponse> customGlobalException(CustomGlobalException ex){
    	CustomErrorResponse error = new CustomErrorResponse();
    	if(ex.getCode() <= 500)
    	{
    		error.setTimestamp(LocalDateTime.now());
    		error.setError(ex.getMessage());
    		error.setStatus(ex.getCode());
    		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.NOT_FOUND);
    	}
    	else {
    		error.setTimestamp(LocalDateTime.now());
    		error.setError(ex.getMessage());
    		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	    		
    }
}

