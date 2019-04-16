package com.hcl.bss.dto;

import java.util.List;

public class UserAuthDto {
	private String userId;
	private String userFirstName;
	private List<MenuAuthDto> menuList;
	
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
	public List<MenuAuthDto> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<MenuAuthDto> menuList) {
		this.menuList = menuList;
	}
	
}
