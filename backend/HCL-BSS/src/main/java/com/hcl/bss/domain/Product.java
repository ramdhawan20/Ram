package com.hcl.bss.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_PRODUCT")
public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "my_sequence")
	@TableGenerator(name = "my_sequence", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val", initialValue = 100000000, allocationSize = 1)
	@Column(name = "UIDPK", nullable = false)
	private Long uidpk;
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
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ID")
	private Product parent;

	//to handle bundle products
	@Column(name="IS_BUNDLE_PRODUCT")
	private int isBundleProduct;

	//to handle multiple children for a parent
	@OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="TB_BUNDLE_PRODUCT_MAPPING",
            joinColumns = @JoinColumn(name="BUNDLE_PRODUCT_UID"),
            inverseJoinColumns = @JoinColumn(name="PRODUCT_UID")
    )
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
	@ManyToMany(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	@JoinTable(name = "TB_RATEPLAN_PRODUCT_MAPPING", joinColumns = { @JoinColumn(name = "PRODUCT_UID",referencedColumnName = "UIDPK")}, inverseJoinColumns = { @JoinColumn(name = "RATEPLAN_UID",referencedColumnName = "UIDPK") })
	private Set<RatePlan> ratePlans = new HashSet<RatePlan>();
	
	public Product() {
	}


	

	public Product(Long uidpk, String productDispName, String sku, String productDescription, Date productExpDate,
			Date productStartDate, ProductTypeMaster productTypeCode, int isActive, Date createdDate, String createdBy,
			Date updatedDate, String updatedBy, Set<RatePlan> ratePlans) {
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
		this.ratePlans = ratePlans;
	}




	public Product(String productDispName, String sku, String productDescription, Date productExpDate,
			Date productStartDate, ProductTypeMaster productTypeCode, int isActive, Date createdDate, String createdBy,
			Date updatedDate, String updatedBy, Set<RatePlan> ratePlans) {
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
		this.ratePlans = ratePlans;
	}


	public int getIsBundleProduct() {
		return isBundleProduct;
	}

	public void setIsBundleProduct(int isBundleProduct) {
		this.isBundleProduct = isBundleProduct;
	}

	public Long getUidpk() {
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

	public Product getParent() {
		return parent;
	}

	public void setParent(Product parent) {
		this.parent = parent;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	public Set<RatePlan> getRatePlans() {
		return this.ratePlans;
	}

	public void setRatePlans(Set<RatePlan> ratePlans) {
		this.ratePlans = ratePlans;
	}

	@Override
	public String toString() {
		return "Product [uidpk=" + uidpk + ", productDispName=" + productDispName + ", sku=" + sku + ", productExpDate="
				+ productExpDate + ", productStartDate=" + productStartDate + ", productTypeCode=" + productTypeCode
				+ ", isActive=" + isActive + ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy + "]";
	}

}
