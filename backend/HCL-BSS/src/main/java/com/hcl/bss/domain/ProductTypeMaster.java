package com.hcl.bss.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_PRODUCT_TYPE_MASTER")
public class ProductTypeMaster implements java.io.Serializable {

	private static final long serialVersionUID = 1L; 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*@OneToMany(mappedBy="productTypeCode", cascade = CascadeType.ALL)
	Set product = new HashSet();*/
	@Column(name = "PRODUCT_TYPE_CODE", nullable = false)
	private String productTypeCode;
	@Column(name = "PRODUCT_TYPE", nullable = false, length = 45)
	private String productType;
	@Column(name = "DESC",length = 100)
	private String discription;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CRE_DT", nullable = false, length = 3)
	private Date createdDate;
	@Column(name = "CRE_BY", length = 50)
	private String createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPD_DT", nullable = false, length = 3)
	private Date updatedDate;
	@Column(name = "UPD_BY", length = 50)
	private String updatedBy;
	public ProductTypeMaster() {
		super();
	}
	public ProductTypeMaster(String productType, String discription, Date createdDate, String createdBy,
			Date updatedDate, String updatedBy) {
		super();
		this.productType = productType;
		this.discription = discription;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}
	@Override
	public String toString() {
		return "ProductTypeMaster [productTypeCode=" + productTypeCode + ", productType=" + productType
				+ ", discription=" + discription + ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy + "]";
	}
	public String getProductTypeCode() {
		return productTypeCode;
	}
	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	/*public Set getProduct() {
		return product;
	}
	public void setProduct(Set product) {
		this.product = product;
	}*/
	
	
	
}
