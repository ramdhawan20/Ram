package com.hcl.bss.exceptions;

public class CustomGlobalException extends RuntimeException{
	
	private int code;
	private String message;
	public CustomGlobalException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
