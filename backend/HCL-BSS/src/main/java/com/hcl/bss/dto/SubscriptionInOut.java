package com.hcl.bss.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.hcl.bss.domain.Subscription1;
/**
 *
 * @author- Vinay Panwar
 */
public class SubscriptionInOut {
	/*For Filter*/
	private String subscriptionId;
	//@NotEmpty @Email
	private String customerName;
	//private String email;
	private String planName;
	private String status;
/*	private double price;
	private Date createdDate;
	private Date activatedDate;
	private Date lastBillDate;
	private Date nextBillDate;
*/  private String fromDateStr;
	private String toDateStr;
	/*For Pagination*/
	private int pageNo;
	private int totalPages;
	private boolean lastPage;
	/*For Response*/
	private Boolean success;
	private String message;
	private List<SubscriptionDto> subscriptionList;
	private int responseCode;
	
	public String getSubscriptionId() {
		return subscriptionId;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getFromDateStr() {
		return fromDateStr;
	}

	public void setFromDateStr(String fromDateStr) {
		this.fromDateStr = fromDateStr;
	}

	public String getToDateStr() {
		return toDateStr;
	}

	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<SubscriptionDto> getSubscriptionList() {
		return subscriptionList;
	}
	public void setSubscriptionList(List<SubscriptionDto> subscriptionList) {
		this.subscriptionList = subscriptionList;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	@Override
	public String toString() {
		return "SubscriptionInOut [subscriptionId=" + subscriptionId + ", customerName=" + customerName + ", planName="
				+ planName + ", status=" + status + ", fromDateStr=" + fromDateStr + ", toDateStr=" + toDateStr + "]";
	}
	
	
	
}
