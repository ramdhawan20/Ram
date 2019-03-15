package com.hcl.bss.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "TB_PRODUCT")
public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "my_sequence")
	@TableGenerator(name = "my_sequence", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val", initialValue = 100000000, allocationSize = 1)
	@Column(name = "UIDPK", nullable = false)
	private long uidpk;
	@Column(name = "PRODUCT_DISP_NAME", nullable = false, length = 100)
	private String productDispName;
	@Column(name = "SKU", nullable = false, length = 20)
	private String sku;
	@Column(name = "PRODUCT_DESCRIPTION", length = 100)
	private String productDescription;
	@Column(name = "PRODUCT_EXP_DT", nullable = false)
	private Date productExpDate;
	@Column(name = "PRODUCT_START_DT")
	private Date productStartDate;
	@ManyToOne /* (cascade = {CascadeType.MERGE}) */
	@JoinColumn(name = "PRODUCT_TYPE_CODE", referencedColumnName = "PRODUCT_TYPE_CODE")
	private ProductTypeMaster productTypeCode;

	// to handle parent for a product
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PARENT_ID")
	private Product parent;

	//to handle bundle products
	@ManyToMany
	private Set<Product> bundles = new HashSet<>();

	//to handle multiple children for a parent
	@OneToMany(mappedBy = "parent")
	private Set<Product> children = new HashSet<>();

	@Column(name = "IS_ACTIVE", nullable = false)
	private int isActive;
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

	public Product() {
	}

	public Product(String productDispName, String sku, Date productExpDate, Date productStartDate,
				   ProductTypeMaster productTypeCode, int isActive, Date createdDate, String createdBy, Date updatedDate,
				   String updatedBy) {
		super();
		this.productDispName = productDispName;
		this.sku = sku;
		this.productExpDate = productExpDate;
		this.productStartDate = productStartDate;
		this.productTypeCode = productTypeCode;
		this.isActive = isActive;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}

	public Product(long uidpk, String productDispName, String sku, String productDescription, Date productExpDate,
				   Date productStartDate, ProductTypeMaster productTypeCode, int isActive, Date createdDate, String createdBy,
				   Date updatedDate, String updatedBy) {
		super();
		this.uidpk = uidpk;
		this.productDispName = productDispName;
		this.sku = sku;
		this.productDescription = productDescription;
		this.productExpDate = productExpDate;
		this.productStartDate = productStartDate;
		this.productTypeCode = productTypeCode;
		this.isActive = isActive;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}

	public long getUidpk() {
		return uidpk;
	}

	public void setUidpk(long uidpk) {
		this.uidpk = uidpk;
	}

	public String getProductDispName() {
		return productDispName;
	}

	public void setProductDispName(String productDispName) {
		this.productDispName = productDispName;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Date getProductExpDate() {
		return productExpDate;
	}

	public void setProductExpDate(Date productExpDate) {
		this.productExpDate = productExpDate;
	}

	public Date getProductStartDate() {
		return productStartDate;
	}

	public void setProductStartDate(Date productStartDate) {
		this.productStartDate = productStartDate;
	}

	public ProductTypeMaster getProductTypeCode() {
		return productTypeCode;
	}

	public void setProductTypeCode(ProductTypeMaster productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
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

	public String getProductDescription() {
		return productDescription;
	}

	public Product(String productDispName, String sku, String productDescription, Date productExpDate,
				   Date productStartDate, ProductTypeMaster productTypeCode, int isActive, Date createdDate, String createdBy,
				   Date updatedDate, String updatedBy) {
		super();
		this.productDispName = productDispName;
		this.sku = sku;
		this.productDescription = productDescription;
		this.productExpDate = productExpDate;
		this.productStartDate = productStartDate;
		this.productTypeCode = productTypeCode;
		this.isActive = isActive;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	@Override
	public String toString() {
		return "Product [uidpk=" + uidpk + ", productDispName=" + productDispName + ", sku=" + sku + ", productExpDate="
				+ productExpDate + ", productStartDate=" + productStartDate + ", productTypeCode=" + productTypeCode
				+ ", isActive=" + isActive + ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy + "]";
	}

}
