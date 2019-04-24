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
 * @author- Vinay Panwar
 */
@Entity
@Table(name="TB_ROLE_MENU_MAPPING")
@Proxy(lazy = false)
public class RoleMenuMapping implements Serializable {
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

    @Column(name="ROLE_UID")
    private Long roleUid;
    @Column(name="MENU_UID")
    private Long menuUid;
    @Column(name="SUB_MENU_UID")
    private Long subMenuUid;
    @CreatedBy
    @Column(name = "CRE_BY")
    private String createdBy;
    @CreatedDate
    @Column(name = "CRE_DT")
    private Timestamp createdDate;
    @LastModifiedBy
    @Column(name = "UPD_BY")
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
	public Long getMenuUid() {
		return menuUid;
	}
	public void setMenuUid(Long menuUid) {
		this.menuUid = menuUid;
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
	public Long getRoleUid() {
		return roleUid;
	}
	public void setRoleUid(Long roleUid) {
		this.roleUid = roleUid;
	}
	public Long getSubMenuUid() {
		return subMenuUid;
	}
	public void setSubMenuUid(Long subMenuUid) {
		this.subMenuUid = subMenuUid;
	}
	
	@Override
	public String toString() {
		return "ProfileMapping [id=" + id + ", roleUid=" + roleUid + ", menuUid=" + menuUid + ", subMenuUid="
				+ subMenuUid + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + "]";
	}

}
