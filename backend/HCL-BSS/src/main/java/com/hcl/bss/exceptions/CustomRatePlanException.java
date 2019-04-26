package com.hcl.bss.exceptions;

public class CustomRatePlanException extends CustomGlobalException {
	public CustomRatePlanException (int code, String message){
        super(code,message);
    }	
}
