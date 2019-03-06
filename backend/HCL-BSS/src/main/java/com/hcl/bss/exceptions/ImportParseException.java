package com.hcl.bss.exceptions;

public class ImportParseException extends RuntimeException {
	 private static final long serialVersionUID = 1L;
	 private String errorMessage;
	  
	    public String getErrorMessage() {
	        return errorMessage;
	    }
	public ImportParseException(String errorMessage){  
		  super(errorMessage);  
		  this.errorMessage = errorMessage;
		 }  
	public ImportParseException() {
        super();
    }
	
}
