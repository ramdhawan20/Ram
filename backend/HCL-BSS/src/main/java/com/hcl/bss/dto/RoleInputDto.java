package com.hcl.bss.dto;

public class RoleInputDto {
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "RoleInputDto [roleName=" + roleName + "]";
	}
	
}
