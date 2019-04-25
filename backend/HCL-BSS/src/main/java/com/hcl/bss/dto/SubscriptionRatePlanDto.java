package com.hcl.bss.dto;

public class SubscriptionRatePlanDto {
	
	private String rateplan;
	private String rateplanDesc;
	private String productName;
	private Integer quantity;
	private String rate;
	private int tax;
	private String amount;
	private Boolean isParent;
	public String getRateplan() {
		return rateplan;
	}
	public void setRateplan(String rateplan) {
		this.rateplan = rateplan;
	}
	public String getRateplanDesc() {
		return rateplanDesc;
	}
	public void setRateplanDesc(String rateplanDesc) {
		this.rateplanDesc = rateplanDesc;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
}
