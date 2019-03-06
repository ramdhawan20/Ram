package com.hcl.bss.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;;

@Entity
//@Table(name = "TB_SUBSCRIPTION")
//@NamedQueries({ @NamedQuery(name = "findUserById", query = "from User a where a.id = :id"), })
//@NamedQueries({ @NamedQuery(name = "findUserById", query = "from User a where a.userId = :userId"), })
public class Subscription1 {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name = "uidpk")
	private int id;
	//@Column(name = "SUBSCRIPTION_ID")
	private String subscriptionId;
	//@NotEmpty @Email
	//@Column(name = "user_id")
	private String customerName;
	//@Column(name = "user_id")
	private String email;
	//@Column(name = "user_id")
	private String planName;
	//@Column(name = "user_id")
	private String status;
	//@Column(name = "user_id")
	private BigDecimal price;
	//@Column(name = "user_id")
	private Date createdDate;
	//@Column(name = "user_id")
	private Date activatedDate;
	//@Column(name = "user_id")
	private Date lastBillDate;
	//@Column(name = "user_id")
	private Date nextBillDate;
	
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getActivatedDate() {
		return activatedDate;
	}
	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}
	public Date getLastBillDate() {
		return lastBillDate;
	}
	public void setLastBillDate(Date lastBillDate) {
		this.lastBillDate = lastBillDate;
	}
	public Date getNextBillDate() {
		return nextBillDate;
	}
	public void setNextBillDate(Date nextBillDate) {
		this.nextBillDate = nextBillDate;
	}
	
	
}