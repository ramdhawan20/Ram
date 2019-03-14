package com.hcl.bss.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * This is SubscriptionDto for subscriptions
 *
 * @author- Vinay Panwar
 */
public class SubscriptionDto {
	private int id;
	private String subscriptionId;
	//@NotEmpty @Email
	//@Column(name = "user_id")
	private String customerName;
	private String email;
	//private String planName;
	//private BigDecimal price;
	private List<MultipleRatePlanDto> ratePlanList;
	private String status;
	private String createdDate;
	private String activatedDate;
	private String lastBillDate;
	private String nextBillDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSubscriptionId() {
		return subscriptionId;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
/*	public Set<String> getPlanName() {
		return planName;
	}
	public void setPlanName(Set<String> planName) {
		this.planName = planName;
	}
*/	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
/*	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
*/
	
	
	public List<MultipleRatePlanDto> getRatePlanList() {
		return ratePlanList;
	}
	
	public void setRatePlanList(List<MultipleRatePlanDto> ratePlanList) {
		this.ratePlanList = ratePlanList;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getActivatedDate() {
		return activatedDate;
	}
	public void setActivatedDate(String activatedDate) {
		this.activatedDate = activatedDate;
	}
	public String getLastBillDate() {
		return lastBillDate;
	}
	public void setLastBillDate(String lastBillDate) {
		this.lastBillDate = lastBillDate;
	}
	public String getNextBillDate() {
		return nextBillDate;
	}
	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}
	
}