package com.hcl.bss.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author- Aditya gupta
 */
@Entity
@Table(name="TB_RATEPLAN_MASTER")
@Proxy(lazy = false)
public class RatePlan implements Serializable {
    @Id
    @Column(name="UIDPK")
    private Long id;
    @Column(name="RATEPLAN_ID")
    private String ratePlanId;
    @Column(name="RATEPLAN_DESC")
    private String ratePlanDescription;
    @Column(name="BILLING_FREQUENCY")
    private String billingFrequency;
    @Column(name="PRICE")
    private double price;
    @OneToOne
    @JoinColumn(name = "UNIT_OF_MEASUREID", referencedColumnName = "UNIT_OF_MEASURE")
    private UOM uom;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="ratePlan")
    //@JoinColumn(name="RATE_PLAN_UID")
    private Set<SubscriptionRatePlan> subscriptionRatePlans;
    @Column(name="IS_ACTIVE")
    private Integer isActive;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="FREQUENCY_CODE")
    private String frequencyCode;
    @Column(name="BILLING_CYCLE_TERM")
    private int billingCycleTerm;
    @Column(name="FREE_TRIAL")
    private int freeTrial;
    @Column(name="SETUP_FEE")
    private int setupFee;
    @Column(name="EXPIRES_AFTER")
    private int expiresAfter;

    @CreatedBy
    @Column(name = "CRE_BY")
    private String createdBy;
    @CreatedDate
    @Column(name = "CRE_DT")
    private Timestamp createdDate;
    @Column(name = "UPD_BY")
    @LastModifiedBy
    private String updatedBy;
    @LastModifiedDate
    @Column(name = "UPD_DT")
    private Timestamp updatedDate;
    
    @ManyToMany(mappedBy="ratePlans")
    @Cascade({org.hibernate.annotations.CascadeType.ALL}) 
	private Set<Product> products = new HashSet<Product>();

    public Set<SubscriptionRatePlan> getSubscriptionRatePlans() {
        return subscriptionRatePlans;
    }

    public void setSubscriptionRatePlans(Set<SubscriptionRatePlan> subscriptionRatePlans) {
        this.subscriptionRatePlans = subscriptionRatePlans;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRatePlanId() {
        return ratePlanId;
    }

    public void setRatePlanId(String ratePlanId) {
        this.ratePlanId = ratePlanId;
    }

    public String getRatePlanDescription() {
        return ratePlanDescription;
    }

    public void setRatePlanDescription(String ratePlanDescription) {
        this.ratePlanDescription = ratePlanDescription;
    }

    public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(UOM uom) {
        this.uom = uom;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public int getBillingCycleTerm() {
        return billingCycleTerm;
    }

    public void setBillingCycleTerm(int billingCycleTerm) {
        this.billingCycleTerm = billingCycleTerm;
    }

    public int getFreeTrial() {
        return freeTrial;
    }

    public void setFreeTrial(int freeTrial) {
        this.freeTrial = freeTrial;
    }

    public int getSetupFee() {
        return setupFee;
    }

    public void setSetupFee(int setupFee) {
        this.setupFee = setupFee;
    }

    public int getExpiresAfter() {
        return expiresAfter;
    }

    public void setExpiresAfter(int expiresAfter) {
        this.expiresAfter = expiresAfter;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "RatePlan [id=" + id + ", ratePlanId=" + ratePlanId + ", ratePlanDescription=" + ratePlanDescription
				+ ", price=" + price + ", uom=" + uom + ", isActive=" + isActive + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate
				+ ", products=" + products + "]";
	}
    
}
