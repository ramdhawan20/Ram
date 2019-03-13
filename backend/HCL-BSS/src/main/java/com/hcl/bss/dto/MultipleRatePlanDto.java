package com.hcl.bss.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;;
/**
 * This is MultipleRatePlanDto for subscriptions
 *
 * @author- Vinay Panwar
 */
public class MultipleRatePlanDto {
	private String planName;
	private double price;
	//private String status;
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
/*	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
*/	

}