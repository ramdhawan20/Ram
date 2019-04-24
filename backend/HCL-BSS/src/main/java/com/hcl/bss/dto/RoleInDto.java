package com.hcl.bss.dto;

import javax.validation.constraints.NotEmpty;

public class RoleInDto {
	@NotEmpty
	private String roleName;
	private String description;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "RoleInDto [roleName=" + roleName + ", description=" + description + "]";
	}
	
}
