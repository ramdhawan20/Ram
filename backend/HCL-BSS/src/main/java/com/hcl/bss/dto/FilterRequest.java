package com.hcl.bss.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class FilterRequest {

	private String startDate;
	private String endDate;
	private String status;
	private String pageNo;
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
}
