package com.hcl.bss.domain;

import com.hcl.bss.repository.generator.LoggedUserGenerator;
import org.hibernate.annotations.GeneratorType;
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
@Table(name="tb_temporder")
public class Order implements Serializable {

    @Column(name = "UIDPK")
    @Id
    private Long id;
    @Column(name="ORDER_NUMBER")
    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderErrors> orderErors;

    @Column(name="BILLTO_FIRSTNAME")
    private String billToFirstName;

    @Column(name="BILLTO_LASTNAME")
    private String billToLastName;

    @Column(name="BILLTO_EMAIL")
    private String billToEmail;

    @Column(name="BILLTO_PHONENO")
    private String billToPhone;

    @Column(name="BILLTO_ADDRESS_LINE1")
    private String billToAddrLine1;

    @Column(name="BILLTO_ADDRESS_LINE2")
    private String billToAddrLine2;

    @Column(name="BILLTO_ZIP_CODE")
    private String billToZipcode;

    @Column(name="BILLTO_CITY")
    private String billToCity;
    @Column(name="BILLTO_STATE")
    private String billToState;
    @Column(name="BILLTO_COUNTRY")
    private String billToCountry;

    @Column(name="SOLDTO_FIRSTNAME")
    private String soldToFirstName;
    @Column(name="SOLDTO_LASTNAME")
    private String soldToLastName;
    @Column(name="SOLDTO_EMAIL")
    private String soldToEmail;
    @Column(name="SOLDTO_PHONENO")
    private String soldToPhone;
    @Column(name="SOLDTO_ADDRESS_LINE1")
    private String soldToAddrLine1;
    @Column(name="SOLDTO_ADDRESS_LINE2")
    private String soldToAddrLine2;
    @Column(name="SOLDTO_ZIP_CODE")
    private String soldToZipcode;
    @Column(name="SOLDTO_CITY")
    private String soldToCity;
    @Column(name="SOLDTO_STATE")
    private String soldToState;
    @Column(name="SOLDTO_COUNTRY")
    private String soldToCountry;

    @Column(name="COMPANY_NAME")
    private String companyName;

    @Column(name="LOCALE")
    private String locale;
    @Column(name="CURRENCY")
    private String currencyCode;
    @Column(name="ORDER_SOURCE")
    private String orderSourceCode;

    @Column(name="IS_CORPORATE")
    private Integer isCorporate;
    @Column(name="AUTORENEW")
    private Integer autoRenew;
    @Column(name="BILLING_CYCLE")
    private Integer billingCycle;

    @Column(name="BILLING_FREQUENCY")
    private String billingFrequency;
    @Column(name="PRICING_SCHEME_CODE")
    private String pricingSchemeCode;
    @Column(name="RATE_PLAN_ID")
    private Long ratePlanId;
    /*@Column(name="RATEPLAN_VOLUME_UID")
    private Long ratePlanVolumeId;*/
    @Column(name="PRODUCT_ID")
    private Long productId;
    @Column(name="STATUS")
    private String status;

/*    @Column(name="TOTAL_PRICE")
    private Double totalPrice;*/
    @Column(name="QUANTITY")
    private Integer quantity;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBillToFirstName() {
        return billToFirstName;
    }

    public void setBillToFirstName(String billToFirstName) {
        this.billToFirstName = billToFirstName;
    }

    public Integer getIsCorporate() {
        return isCorporate;
    }

    public void setIsCorporate(Integer isCorporate) {
        this.isCorporate = isCorporate;
    }

    public String getBillToLastName() {
        return billToLastName;
    }

    public void setBillToLastName(String billToLastName) {
        this.billToLastName = billToLastName;
    }

    public String getBillToEmail() {
        return billToEmail;
    }

    public void setBillToEmail(String billToEmail) {
        this.billToEmail = billToEmail;
    }

    public String getBillToPhone() {
        return billToPhone;
    }

    public void setBillToPhone(String billToPhone) {
        this.billToPhone = billToPhone;
    }

    public String getBillToAddrLine1() {
        return billToAddrLine1;
    }

    public void setBillToAddrLine1(String billToAddrLine1) {
        this.billToAddrLine1 = billToAddrLine1;
    }

    public String getBillToAddrLine2() {
        return billToAddrLine2;
    }

    public void setBillToAddrLine2(String billToAddrLine2) {
        this.billToAddrLine2 = billToAddrLine2;
    }

    public String getBillToZipcode() {
        return billToZipcode;
    }

    public void setBillToZipcode(String billToZipcode) {
        this.billToZipcode = billToZipcode;
    }

    public String getBillToCity() {
        return billToCity;
    }

    public void setBillToCity(String billToCity) {
        this.billToCity = billToCity;
    }

    public String getBillToState() {
        return billToState;
    }

    public void setBillToState(String billToState) {
        this.billToState = billToState;
    }

    public String getBillToCountry() {
        return billToCountry;
    }

    public void setBillToCountry(String billToCountry) {
        this.billToCountry = billToCountry;
    }

    public String getSoldToFirstName() {
        return soldToFirstName;
    }

    public void setSoldToFirstName(String soldToFirstName) {
        this.soldToFirstName = soldToFirstName;
    }

    public String getSoldToLastName() {
        return soldToLastName;
    }

    public void setSoldToLastName(String soldToLastName) {
        this.soldToLastName = soldToLastName;
    }

    public String getSoldToEmail() {
        return soldToEmail;
    }

    public void setSoldToEmail(String soldToEmail) {
        this.soldToEmail = soldToEmail;
    }

    public String getSoldToPhone() {
        return soldToPhone;
    }

    public void setSoldToPhone(String soldToPhone) {
        this.soldToPhone = soldToPhone;
    }

    public String getSoldToAddrLine1() {
        return soldToAddrLine1;
    }

    public void setSoldToAddrLine1(String soldToAddrLine1) {
        this.soldToAddrLine1 = soldToAddrLine1;
    }

    public String getSoldToAddrLine2() {
        return soldToAddrLine2;
    }

    public void setSoldToAddrLine2(String soldToAddrLine2) {
        this.soldToAddrLine2 = soldToAddrLine2;
    }

    public String getSoldToZipcode() {
        return soldToZipcode;
    }

    public void setSoldToZipcode(String soldToZipcode) {
        this.soldToZipcode = soldToZipcode;
    }

    public String getSoldToCity() {
        return soldToCity;
    }

    public void setSoldToCity(String soldToCity) {
        this.soldToCity = soldToCity;
    }

    public String getSoldToState() {
        return soldToState;
    }

    public void setSoldToState(String soldToState) {
        this.soldToState = soldToState;
    }

    public String getSoldToCountry() {
        return soldToCountry;
    }

    public void setSoldToCountry(String soldToCountry) {
        this.soldToCountry = soldToCountry;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getOrderSourceCode() {
        return orderSourceCode;
    }

    public void setOrderSourceCode(String orderSourceCode) {
        this.orderSourceCode = orderSourceCode;
    }

    public Integer getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Integer autoRenew) {
        this.autoRenew = autoRenew;
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

    public String getPricingSchemeCode() {
        return pricingSchemeCode;
    }

    public void setPricingSchemeCode(String pricingSchemeCode) {
        this.pricingSchemeCode = pricingSchemeCode;
    }

    public Long getRatePlanId() {
        return ratePlanId;
    }

    public void setRatePlanId(Long ratePlanId) {
        this.ratePlanId = ratePlanId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

/*    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }*/

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<OrderErrors> getOrderErors() {
        return orderErors;
    }

    public void setOrderErors(Set<OrderErrors> orderErors) {
        this.orderErors = orderErors;
    }

}
