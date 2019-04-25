package com.hcl.bss.notification.dto;

import java.io.Serializable;

public class SubscriptionRatePlanDto implements Serializable{
	
	private String rateplan;
	private String rateplanDesc;
	private String productName;
	private Integer quantity;
	private double rate;
	private int tax;
	private double amount;
	private String billFrequency;
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
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getBillFrequency() {
		return billFrequency;
	}
	public void setBillFrequency(String billFrequency) {
		this.billFrequency = billFrequency;
	}
	
}

