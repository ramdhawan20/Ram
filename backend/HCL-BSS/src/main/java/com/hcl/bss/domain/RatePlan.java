package com.hcl.bss.domain;

import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
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
    @Column(name="PRICE")
    private double price;
    @OneToOne
    @JoinColumn(name = "UNIT_OF_MEASUREID", referencedColumnName = "UNIT_OF_MEASURE")
    private UOM uom;
    @OneToMany
    @JoinColumn(name="RATE_PLAN_UID")
    private Set<SubscriptionRatePlan> subscriptionRatePlans;
    @Column(name="IS_ACTIVE")
    private Integer isActive;


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
}
