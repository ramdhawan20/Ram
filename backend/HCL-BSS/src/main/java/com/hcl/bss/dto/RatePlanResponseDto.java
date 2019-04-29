package com.hcl.bss.dto;

import java.util.List;

public class RatePlanResponseDto {
	private boolean lastPage;
	private long totalPages;
	private List<RatePlanDto> ratePlanList;
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
	public List<RatePlanDto> getRatePlanList() {
		return ratePlanList;
	}
	public void setRatePlanList(List<RatePlanDto> ratePlanList) {
		this.ratePlanList = ratePlanList;
	}	
}
