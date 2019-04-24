package com.hcl.bss.dto;

import java.util.List;
import java.util.Map;

public class MenuAuthDto {
	private String menuName;
	//private List<SubMenuAuthDto> subManuList;
	private List<String> subManuList;
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public List<String> getSubManuList() {
		return subManuList;
	}
	public void setSubManuList(List<String> subManuList) {
		this.subManuList = subManuList;
	}
	
}
