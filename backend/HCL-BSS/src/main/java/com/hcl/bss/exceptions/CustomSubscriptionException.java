package com.hcl.bss.exceptions;

public class CustomSubscriptionException extends RuntimeException{

	private Integer code;  
	public CustomSubscriptionException(Integer code) {
		super();
		this.code = code;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
}
