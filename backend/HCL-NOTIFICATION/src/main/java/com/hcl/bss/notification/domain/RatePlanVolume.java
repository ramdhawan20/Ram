package com.hcl.bss.notification.domain;

import com.hcl.bss.notification.repository.generator.LoggedUserGenerator;
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
@Proxy(lazy=false)
@Table(name="TB_RATEPLAN_VOLUME_MASTER")
public class RatePlanVolume implements Serializable{
    @Column(name = "UIDPK")
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
    @Column(name = "START_QTY")
    private Integer startQty;
    @Column(name = "END_QTY")
    private Integer endQty;
    @Column(name = "PRICE")
    private Double price;
    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_UID")
    private RatePlan ratePlanUid;
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

    public Integer getStartQty() {
        return startQty;
    }

    public void setStartQty(Integer startQty) {
        this.startQty = startQty;
    }

    public Integer getEndQty() {
        return endQty;
    }

    public void setEndQty(Integer endQty) {
        this.endQty = endQty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public RatePlan getRatePlanUid() {
        return ratePlanUid;
    }

    public void setRatePlanUid(RatePlan ratePlanUid) {
        this.ratePlanUid = ratePlanUid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

}
