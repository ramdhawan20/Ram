package com.hcl.bss.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class MenuAuthDto {
	private String menuName;
	private List<SubMenuAuthDto> subManuList;
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public List<SubMenuAuthDto> getSubManuList() {
		return subManuList;
	}
	public void setSubManuList(List<SubMenuAuthDto> subManuList) {
		this.subManuList = subManuList;
	}

}
