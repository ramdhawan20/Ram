package com.hcl.bss.exceptions;

/**
 * Customized exception class for Authentication related services.
 *
 * author: dhiraj.s
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException (String message){
        super(message);
    }
}
