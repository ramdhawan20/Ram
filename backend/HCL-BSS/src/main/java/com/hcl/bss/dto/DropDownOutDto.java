package com.hcl.bss.dto;

import java.util.List;

import com.hcl.bss.domain.User;

public class DropDownOutDto {
	private Boolean success;
	private String message;
	private List<String> dropDownList;
	private int responseCode;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public List<String> getDropDownList() {
		return dropDownList;
	}
	public void setDropDownList(List<String> dropDownList) {
		this.dropDownList = dropDownList;
	}
	
}
