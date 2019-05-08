package com.hcl.bss.dto;

import java.util.List;

public class RoleResponseDto {
	
	private List<RoleInDto> roleDtoList;
	private boolean lastPage;
	private long totalPages;
	public List<RoleInDto> getRoleDtoList() {
		return roleDtoList;
	}
	public void setRoleDtoList(List<RoleInDto> roleDtoList) {
		this.roleDtoList = roleDtoList;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	
}
