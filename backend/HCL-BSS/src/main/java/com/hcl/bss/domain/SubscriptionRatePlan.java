package com.hcl.bss.domain;

import com.hcl.bss.repository.generator.LoggedUserGenerator;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author- Aditya gupta
 */
@Entity
@Table(name="TB_RATEPLAN_TRANSACTION")
@Proxy(lazy = false)
public class SubscriptionRatePlan implements Serializable {
    @Id
    @Column(name="UIDPK")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
    @TableGenerator(
            name = "uidpk_sequence",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000000000,
            allocationSize = 1)
    private Long id;
    @Column(name="RATE_PLAN_UID")
    private Long ratePlan;

    @Column(name="PRODUCT_ID")
    private Long product;

    @Column(name="QUANTITY")
    private Integer quantity;

    @Column(name="PRICE")
    private double price;
    @Column(name="BILLING_CYCLE")
    private Integer billingCycle;

    @Column(name="BILLING_FREQUENCY")
    private String billingFrequency;

    @ManyToOne
    @JoinColumn(name="PRICING_SCHEME",nullable = false)
    private PricingScheme pricingScheme;

    @Column(name="CRE_DT")
    @CreatedDate
    private Timestamp createdDate;
    @Column(name="CRE_BY")
    @GeneratorType(type = LoggedUserGenerator.class)
    private String createdBy;
    @Column(name="UPD_DT")
    @LastModifiedDate
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name="RATEPLAN_VOLUME_UID", nullable = false)
    private RatePlanVolume ratePlanVolume;
    @Column(name="UPD_BY")
    @GeneratorType(type = LoggedUserGenerator.class)
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRatePlan() {
        return ratePlan;
    }

    public void setRatePlan(Long ratePlan) {
        this.ratePlan = ratePlan;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Integer billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getBillingFrequency() {
        return billingFrequency;
    }

    public void setBillingFrequency(String billingFrequency) {
        this.billingFrequency = billingFrequency;
    }

    public PricingScheme getPricingScheme() {
        return pricingScheme;
    }

    public void setPricingScheme(PricingScheme pricingScheme) {
        this.pricingScheme = pricingScheme;
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

    public RatePlanVolume getRatePlanVolume() {
        return ratePlanVolume;
    }

    public void setRatePlanVolume(RatePlanVolume ratePlanVolume) {
        this.ratePlanVolume = ratePlanVolume;
    }
}
