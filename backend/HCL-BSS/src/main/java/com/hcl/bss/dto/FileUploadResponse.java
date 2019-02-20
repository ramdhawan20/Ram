package com.hcl.bss.dto;

public class FileUploadResponse {

	private Integer noOfFailRecords;
	private Integer noOfSuccessRecords;
	private String status;
	private Integer noOfRecords;
	private String dateAdded;
	private String uploadFileName;
	private String errorLogFileName;

	public Integer getNoOfFailRecords() {
		return noOfFailRecords;
	}

	public void setNoOfFailRecords(Integer noOfFailRecords) {
		this.noOfFailRecords = noOfFailRecords;
	}

	public Integer getNoOfSuccessRecords() {
		return noOfSuccessRecords;
	}

	public void setNoOfSuccessRecords(Integer noOfSuccessRecords) {
		this.noOfSuccessRecords = noOfSuccessRecords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNoOfRecords() {
		return noOfRecords;
	}

	public void setNoOfRecords(Integer noOfRecords) {
		this.noOfRecords = noOfRecords;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getErrorLogFileName() {
		return errorLogFileName;
	}

	public void setErrorLogFileName(String errorLogFileName) {
		this.errorLogFileName = errorLogFileName;
	}

}
