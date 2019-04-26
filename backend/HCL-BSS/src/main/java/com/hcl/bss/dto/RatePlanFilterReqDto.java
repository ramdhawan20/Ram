package com.hcl.bss.dto;

import java.math.BigDecimal;

public class RatePlanFilterReqDto {

	private String planName;
	private String planCode;
	private BigDecimal billCycleTerm;
	private String billFreq;
	private String planType;
	private String status;
	private String pageNo;
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public BigDecimal getBillCycleTerm() {
		return billCycleTerm;
	}
	public void setBillCycleTerm(BigDecimal billCycleTerm) {
		this.billCycleTerm = billCycleTerm;
	}
	public String getBillEvery() {
		return billFreq;
	}
	public void setBillEvery(String billEvery) {
		this.billFreq = billEvery;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getBillFreq() {
		return billFreq;
	}
	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
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
