package com.hcl.bss.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.hcl.bss.config.CustomDateScheme;

@CustomDateScheme.List({
		@CustomDateScheme(field = "productExpDate", message = "Date format is Invalid! should be MM/dd/yyyy") })
public class ProductDto {
	private String productId;
	private long uidpk;
	@NotNull(message = "SKU cannot be null")
	@NotBlank(message = "SKU cannot be blank")
	private String sku;
	@NotNull(message = "Product Name cannot be null")
	@NotBlank(message = "Product Name cannot be blank")
	@Size(max = 10)
	private String productDispName;

	private String productExpDate;

	@NotNull(message = "Product dESCRITION cannot be null")
	@NotBlank(message = "Product dESCRITION cannot be blank")
	@Size(max = 10)
	private String productDescription;
	private String updatedBy;
	private Date updatedDate;
	private String createdBy;
	private Date createdDate;
	private String dateScheme;

	public ProductDto() {
		super();
	}

	public ProductDto(String productId, long uidpk,
			@NotNull(message = "SKU cannot be null") @NotBlank(message = "SKU cannot be blank") String sku,
			@NotNull(message = "Product Name cannot be null") @NotBlank(message = "Product Name cannot be blank") @Size(max = 10) String productDispName,
			String productExpDate,
			@NotNull(message = "Product Description cannot be null") @NotBlank(message = "Product Description cannot be blank") @Size(max = 10) String productDescription,
			String updatedBy, Date updatedDate, String createdBy, Date createdDate) {
		super();
		this.productId = productId;
		this.uidpk = uidpk;
		this.sku = sku;
		this.productDispName = productDispName;
		this.productExpDate = productExpDate;
		this.productDescription = productDescription;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	public ProductDto(String productId, String productDispName, String sku, String productExpDate,
			String productDescription) {
		super();
		this.productId = productId;
		this.productDispName = productDispName;
		this.sku = sku;
		this.productExpDate = productExpDate;
		this.productDescription = productDescription;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	@Override
	public String toString() {
		return "ProductDto [productId=" + productId + ", uidpk=" + uidpk + ", sku=" + sku + ", productDispName="
				+ productDispName + ", productExpDate=" + productExpDate + ", productDescription=" + productDescription
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + "]";
	}

}
