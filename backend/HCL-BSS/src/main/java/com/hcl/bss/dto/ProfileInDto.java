package com.hcl.bss.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
/**
*
* @author- Vinay Panwar
*/

public class ProfileInDto {
	@NotEmpty
	private String roleName;
	private String description;
	private List<RoleMenuDto> menuList;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<RoleMenuDto> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<RoleMenuDto> menuList) {
		this.menuList = menuList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ProfileInDto [roleName=" + roleName + ", description=" + description + ", menuList=" + menuList + "]";
	}
	
}
