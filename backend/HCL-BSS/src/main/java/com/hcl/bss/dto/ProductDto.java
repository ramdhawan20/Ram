package com.hcl.bss.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hcl.bss.validator.CustomDateScheme;
import com.hcl.bss.validator.FieldMatch;

@CustomDateScheme.List({
		@CustomDateScheme(field = "productExpDate", message = "Date format is Invalid! should be dd/MM/yyyy or dd-MM-yyyy") })
@FieldMatch.List({
		@FieldMatch(first = "prescribedFileHeader", second = "fileHeader", message = "There is some mismatch in CSV header") })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto implements java.io.Serializable {
	@NotNull(message = "PRODUCT_TYPE_CODE cannot be null")
	@NotBlank(message = "PRODUCT_TYPE_CODE cannot be blank")
	private String productTypeCode;
	private long uidpk;

	@NotNull(message = "SKU cannot be null")
	@NotBlank(message = "SKU cannot be blank")
	@Size(max = 20, message = "SKU  size should not exceed 20")
	private String sku;

	@NotNull(message = "PRODUCT_DISPLAY_NAME cannot be null")
	@NotBlank(message = "PRODUCT_DISPLAY_NAME cannot be blank")
	@Size(max = 100, message = "PRODUCT_DISPLAY_NAME  size should not exceed 100")
	private String productDispName;

	private String productExpDate;

	@NotNull(message = "PRODUCT_DESCRIPTION cannot be null")
	@NotBlank(message = "PRODUCT_DESCRIPTION cannot be blank")
	@Size(max = 100, message = "PRODUCT_DISPLAY_NAME  size should not exceed 100")
	private String productDescription;
	private String productType;
	private String updatedBy;
	private Date updatedDate;
	private String createdBy;
	private Date createdDate;
	private String dateScheme;
	private String prescribedFileHeader;
	private String fileHeader;
	private String productStartDate;

	public ProductDto() {
		super();
	}

	public ProductDto(String productTypeCode, long uidpk,
			@NotNull(message = "SKU cannot be null") @NotBlank(message = "SKU cannot be blank") String sku,
			@NotNull(message = "PRODUCT_DISPLAY_NAME cannot be null") @NotBlank(message = "PRODUCT_DISPLAY_NAME cannot be blank") @Size(max = 100) String productDispName,
			String productExpDate,
			@NotNull(message = "PRODUCT_DESCRIPTION cannot be null") @NotBlank(message = "PRODUCT_DESCRIPTION cannot be blank") @Size(max = 100) String productDescription,
			String updatedBy, Date updatedDate, String createdBy, Date createdDate, String prescribedFileHeader,
			String fileHeader) {
		super();
		this.productTypeCode = productTypeCode;
		this.uidpk = uidpk;
		this.sku = sku;
		this.productDispName = productDispName;
		this.productExpDate = productExpDate;
		this.productDescription = productDescription;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.prescribedFileHeader = prescribedFileHeader;
		this.fileHeader = fileHeader;
	}

	public ProductDto(
			@NotNull(message = "SKU cannot be null") @NotBlank(message = "SKU cannot be blank") String productTypeCode,
			@NotNull(message = "PRODUCT_DISPLAY_NAME cannot be null") @NotBlank(message = "PRODUCT_DISPLAY_NAME cannot be blank") @Size(max = 100) String productDispName,
			@NotNull(message = "SKU cannot be null") @NotBlank(message = "SKU cannot be blank") String sku,
			String productExpDate,
			@NotNull(message = "PRODUCT_DESCRIPTION cannot be null") @NotBlank(message = "PRODUCT_DESCRIPTION cannot be blank") @Size(max = 100) String productDescription) {
		super();
		this.productTypeCode = productTypeCode;
		this.sku = sku;
		this.productDispName = productDispName;
		this.productExpDate = productExpDate;
		this.productDescription = productDescription;

	}

	public String getProductTypeCode() {
		return productTypeCode;
	}

	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
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

	public String getProductExpDate() {
		return productExpDate;
	}

	public void setProductExpDate(String productExpDate) {
		this.productExpDate = productExpDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getDateScheme() {
		return dateScheme;
	}

	public void setDateScheme(String dateScheme) {
		this.dateScheme = dateScheme;
	}

	public String getPrescribedFileHeader() {
		return prescribedFileHeader;
	}

	public void setPrescribedFileHeader(String prescribedFileHeader) {
		this.prescribedFileHeader = prescribedFileHeader;
	}

	public String getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(String fileHeader) {
		this.fileHeader = fileHeader;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductStartDate() {
		return productStartDate;
	}

	public void setProductStartDate(String productStartDate) {
		this.productStartDate = productStartDate;
	}

	@Override
	public String toString() {
		return "ProductDto [productTypeCode=" + productTypeCode + ", uidpk=" + uidpk + ", sku=" + sku
				+ ", productDispName=" + productDispName + ", productExpDate=" + productExpDate
				+ ", productDescription=" + productDescription + ", updatedBy=" + updatedBy + ", updatedDate="
				+ updatedDate + ", createdBy=" + createdBy + ", createdDate=" + createdDate + "]";
	}

}
