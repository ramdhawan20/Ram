package com.hcl.bss.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
/**
*
* @author- Vinay Panwar
*/

public class RoleMenuDto {
	@NotEmpty
	private String menuName;
	private List<String> subMenuList;
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public List<String> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<String> subMenuList) {
		this.subMenuList = subMenuList;
	}
	
}
