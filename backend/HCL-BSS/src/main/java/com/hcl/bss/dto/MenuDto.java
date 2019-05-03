package com.hcl.bss.dto;

import java.util.Map;
import java.util.Set;

public class MenuDto {
	String roleName;
	private Map<String, Set<String>> mappedMenuMap;
	private Map<String, Set<String>> unmappedMenuMap;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Map<String, Set<String>> getMappedMenuMap() {
		return mappedMenuMap;
	}
	public void setMappedMenuMap(Map<String, Set<String>> mappedMenuMap) {
		this.mappedMenuMap = mappedMenuMap;
	}
	public Map<String, Set<String>> getUnmappedMenuMap() {
		return unmappedMenuMap;
	}
	public void setUnmappedMenuMap(Map<String, Set<String>> unmappedMenuMap) {
		this.unmappedMenuMap = unmappedMenuMap;
	}

}
