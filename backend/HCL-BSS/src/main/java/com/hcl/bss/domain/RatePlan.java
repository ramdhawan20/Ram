package com.hcl.bss.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
    @TableGenerator(
            name = "uidpk_sequence",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000000000,
            allocationSize = 1)
    @Column(name="UIDPK")
    private Long id;
    @Column(name="RATEPLAN_ID")
    private String ratePlanId;
    @Column(name="RATEPLAN_DESC")
    private String ratePlanDescription;
    @Column(name="FREQUENCY_CODE")
    private String billingFrequency;
    @Column(name="BILLING_CYCLE_TERM")
    private BigDecimal billingCycleTerm;
    @Column(name="FREE_TRIAL")
    private BigDecimal freeTrail;
    @Column(name="SETUP_FEE")
    private BigDecimal setUpFee;
    @Column(name="EXPIRE_AFTER")
    private BigDecimal expireAfter;

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

    @Column(name="PRICING_SCHEME")
    private String pricingScheme;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="RATE_PLAN_UID", referencedColumnName="UIDPK")
    private List<RatePlanVolume> ratePlanVolume;

    @ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinTable(name = "TB_RATEPLAN_CURRENCY_MAPPING", joinColumns = { @JoinColumn(name = "RATEPLAN_UID",referencedColumnName = "UIDPK")}, inverseJoinColumns = { @JoinColumn(name = "CURRENCY_CODE",referencedColumnName = "CURRENCY_CODE") })
    private Currency currency;

    public List<RatePlanVolume> getRatePlanVolume() {
		return ratePlanVolume;
	}

	public void setRatePlanVolume(List<RatePlanVolume> ratePlanVolume) {
		this.ratePlanVolume = ratePlanVolume;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}


    public String getPricingScheme() {
		return pricingScheme;
	}

	public void setPricingScheme(String pricingScheme) {
		this.pricingScheme = pricingScheme;
	}

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


    public BigDecimal getBillingCycleTerm() {
		return billingCycleTerm;
	}

	public void setBillingCycleTerm(BigDecimal billingCycleTerm) {
		this.billingCycleTerm = billingCycleTerm;
	}

	public BigDecimal getFreeTrail() {
		return freeTrail;
	}

	public void setFreeTrail(BigDecimal freeTrail) {
		this.freeTrail = freeTrail;
	}

	public BigDecimal getSetUpFee() {
		return setUpFee;
	}

	public void setSetUpFee(BigDecimal setUpFee) {
		this.setUpFee = setUpFee;
	}

	public BigDecimal getExpireAfter() {
		return expireAfter;
	}

	public void setExpireAfter(BigDecimal expireAfter) {
		this.expireAfter = expireAfter;
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
