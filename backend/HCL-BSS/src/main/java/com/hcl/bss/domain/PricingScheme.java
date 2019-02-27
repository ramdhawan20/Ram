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
@Table(name="TB_PRICING_SCHEME_MASTER")
@Proxy(lazy = false)
public class PricingScheme implements Serializable {
    @Column(name="UIDPK")
    private Long id;
    @Id
    @Column(name="PRICING_SCHEME_CODE")
    private String pricingSchemeCode;
    @CreatedDate
    @Column(name="CRE_DT")
    private Timestamp createdDate;
    @Column(name="CRE_BY")
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    @Column(name="UPD_DT")
    private Timestamp updatedDate;
    @Column(name="UPD_BY")
    @LastModifiedBy
    private Timestamp updatedBy;
    @OneToMany(mappedBy = "pricingScheme")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPricingSchemeCode() {
        return pricingSchemeCode;
    }

    public void setPricingSchemeCode(String pricingSchemeCode) {
        this.pricingSchemeCode = pricingSchemeCode;
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

    public Timestamp getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Timestamp updatedBy) {
        this.updatedBy = updatedBy;
    }
}
