package com.hcl.bss.dto;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserAuthDto {
	private String userId;
	private String userName;
	private Set<String> roleNameSet;
	private Map<String, Set<String>> menuMap;
	
	public Set<String> getRoleNameSet() {
		return roleNameSet;
	}
	public void setRoleNameSet(Set<String> roleNameSet) {
		this.roleNameSet = roleNameSet;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Map<String, Set<String>> getMenuMap() {
		return menuMap;
	}
	public void setMenuMap(Map<String, Set<String>> menuMap) {
		this.menuMap = menuMap;
	}
	
}
