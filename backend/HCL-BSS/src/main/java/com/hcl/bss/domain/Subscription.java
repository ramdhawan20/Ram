package com.hcl.bss.domain;

import com.hcl.bss.repository.generator.LoggedUserGenerator;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author- Aditya gupta
 */
@Entity
@Table(name="TB_SUBSCRIPTION")
@EntityListeners(AuditingEntityListener.class)
public class Subscription implements Serializable {
    @Column(name="UIDPK")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
    @TableGenerator(
            name = "uidpk_sequence",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000000000,
            allocationSize = 1)
    private Long id;

    @Column(name="SUBSCRIPTION_ID")
    private String subscriptionId;

    @Column(name="CUST_ID",insertable= false, updatable= false)
    private Long customerId;

    @Column(name="ACTIVATION_DT")
    private Timestamp activationDate;
    //@ManyToOne
    @Column(name="TRANSACTION_REASON_CODE_ID")
    private String transactionReasonCode;
    //@ManyToOne
    @Column(name="ORDER_SOURCE_CODE")
    private String orderSourceCode;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBSCRIPTION_UID", referencedColumnName = "UIDPK",nullable = false)
    //@OneToMany(mappedBy = "subscription", fetch = FetchType.EAGER)
    private Set<SubscriptionRatePlan> subscriptionRatePlan = new HashSet<>();
    @Column(name="SUBSCRIPTION_START_DT")
    private Timestamp subscriptionStartDate;
    @Column(name="SUBSCRIPTION_END_DT")
    private Timestamp subscriptionEndDate;
    @Column(name="AUTO_RENEW")
    private Integer autorenew;
    @Column(name="IS_ACTIVE")
    private Integer isActive;
    @Column(name="STATUS")
    private String status;

    @Column(name="LAST_BILL_DT")
    private Date lastBillingDate;
    @Column(name="NEXT_BILL_DT")
    private Date nextBillingDate;

    @GeneratorType(type = LoggedUserGenerator.class)
    @Column(name = "CRE_BY")
    private String createdBy;
    @CreatedDate
    @Column(name = "CRE_DT")
    private Timestamp createdDate;
    @Column(name = "UPD_BY")
    @GeneratorType(type = LoggedUserGenerator.class)
    private String updatedBy;
    @LastModifiedDate
    @Column(name = "UPD_DT")
    private Timestamp updatedDate;


    //@ManyToOne(cascade = CascadeType.ALL)
/*    @JoinColumn(name="CUST_ID")
   //@JoinColumn(name="UIDPK")
    private Customer customer;
    */

/*    public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Timestamp getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Timestamp activationDate) {
        this.activationDate = activationDate;
    }

    public String getTransactionReasonCode() {
        return transactionReasonCode;
    }

    public void setTransactionReasonCode(String transactionReasonCode) {
        this.transactionReasonCode = transactionReasonCode;
    }

    public String getOrderSourceCode() {
        return orderSourceCode;
    }

    public void setOrderSourceCode(String orderSourceCode) {
        this.orderSourceCode = orderSourceCode;
    }

    public Set<SubscriptionRatePlan> getSubscriptionRatePlan() {
        return subscriptionRatePlan;
    }

    public void setSubscriptionRatePlan(SubscriptionRatePlan subscriptionRatePlan) {
        this.subscriptionRatePlan.add(subscriptionRatePlan);
    }

    public Timestamp getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(Timestamp subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public Timestamp getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(Timestamp subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public Integer getAutorenew() {
        return autorenew;
    }

    public void setAutorenew(Integer autorenew) {
        this.autorenew = autorenew;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setSubscriptionRatePlan(Set<SubscriptionRatePlan> subscriptionRatePlan) {
        this.subscriptionRatePlan = subscriptionRatePlan;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getLastBillingDate() {
        return lastBillingDate;
    }

    public void setLastBillingDate(Date lastBillingDate) {
        this.lastBillingDate = lastBillingDate;
    }

    public Date getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(Date nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }
}
