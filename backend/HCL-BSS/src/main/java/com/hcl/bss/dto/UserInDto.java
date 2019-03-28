package com.hcl.bss.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserInDto {
	private String userProfile;
	//@NotEmpty @Email
	private String userId;
	//@NotEmpty
	private String userFirstName;
	private String userMiddleName;
	private String userLastName;
	private String status;
	private String attribute;
	private String newAttribute;
	/*For Pagination*/
	private int pageNo;
	private int totalPages;
	private boolean lastPage;

	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserMiddleName() {
		return userMiddleName;
	}
	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNewAttribute() {
		return newAttribute;
	}
	public void setNewAttribute(String newAttribute) {
		this.newAttribute = newAttribute;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	@Override
	public String toString() {
		return "UserInDto [userProfile=" + userProfile + ", userId=" + userId + ", userFirstName=" + userFirstName
				+ ", userMiddleName=" + userMiddleName + ", userLastName=" + userLastName + ", newAttribute="
				+ newAttribute + ", pageNo=" + pageNo + "]";
	}
		
}
