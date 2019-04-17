package com.hcl.bss.notification.dto;

import java.util.List;

public class BatchDto {
	
	private long success;
	private long failed;
	private boolean lastPage;
	private long totalPages;
	private List<BatchRunLogDto> batchRunLogDtoList;
	public List<BatchRunLogDto> getBatchRunLogDtoList() {
		return batchRunLogDtoList;
	}
	public void setBatchRunLogDtoList(List<BatchRunLogDto> batchRunLogDtoList) {
		this.batchRunLogDtoList = batchRunLogDtoList;
	}
	public long getSuccess() {
		return success;
	}
	public void setSuccess(long success) {
		this.success = success;
	}
	public long getFailed() {
		return failed;
	}
	public void setFailed(long failed) {
		this.failed = failed;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
}
