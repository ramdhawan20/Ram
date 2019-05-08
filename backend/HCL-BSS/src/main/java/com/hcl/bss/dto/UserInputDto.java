package com.hcl.bss.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserInputDto {
	private Set<String> userProfileSet;
	@NotEmpty @Email
	private String userId;
	private String userFirstName;
	private String userMiddleName;
	private String userLastName;
	private String status;
	private String attribute;
	private String newAttribute;

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
	public Set<String> getUserProfileSet() {
		return userProfileSet;
	}
	public void setUserProfileSet(Set<String> userProfileSet) {
		this.userProfileSet = userProfileSet;
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
		return "UserInputDto [userProfileSet=" + userProfileSet + ", userId=" + userId + ", userFirstName="
				+ userFirstName + ", userMiddleName=" + userMiddleName + ", userLastName=" + userLastName + ", status="
				+ status + "]";
	}
		
}
