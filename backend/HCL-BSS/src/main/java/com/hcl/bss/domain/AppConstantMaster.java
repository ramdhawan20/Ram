package com.hcl.bss.domain;

import com.hcl.bss.repository.generator.LoggedUserGenerator;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Proxy(lazy=false)
@Table(name="TB_APP_CONSTANTS_MASTER")

public class AppConstantMaster implements Serializable {


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

    @Column(name="DROPDOWN_CODE")
    private Integer dropdownCode;

    @Column(name="SUB_DROPDOWN_CODE")
    private Integer subDropdownCode;

    @Column(name = "SUB_DROPDOWN_DESC")
    private String subDropdownDesc;

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
	
	public Integer getDropdownCode() {
		return dropdownCode;
	}
	public void setDropdownCode(Integer dropdownCode) {
		this.dropdownCode = dropdownCode;
	}
	public Integer getSubDropdownCode() {
		return subDropdownCode;
	}
	public void setSubDropdownCode(Integer subDropdownCode) {
		this.subDropdownCode = subDropdownCode;
	}
	public String getSubDropdownDesc() {
		return subDropdownDesc;
	}
	public void setSubDropdownDesc(String subDropdownDesc) {
		this.subDropdownDesc = subDropdownDesc;
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
