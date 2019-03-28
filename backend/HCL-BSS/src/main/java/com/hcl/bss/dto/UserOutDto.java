package com.hcl.bss.dto;

import java.util.List;

import com.hcl.bss.domain.User;

public class UserOutDto {
	/*For Pagination*/
	private int totalPages;
	private boolean lastPage;
	/*For Response*/
	private Boolean success;
	private String message;
	private List<UserDto> userList;
	private int responseCode;
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
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
	public List<UserDto> getUserList() {
		return userList;
	}
	public void setUserList(List<UserDto> userList) {
		this.userList = userList;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
