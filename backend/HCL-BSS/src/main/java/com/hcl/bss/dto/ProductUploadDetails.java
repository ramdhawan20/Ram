package com.hcl.bss.dto;

import java.util.List;

public class ProductUploadDetails {

	private List<ProductErrorLogDetails> errorLogDetailsList;
	private List<ProductDto> validProductList;
	
	public List<ProductErrorLogDetails> getErrorLogDetailsList() {
		return errorLogDetailsList;
	}
	public void setErrorLogDetailsList(List<ProductErrorLogDetails> errorLogDetailsList) {
		this.errorLogDetailsList = errorLogDetailsList;
	}
	public List<ProductDto> getValidProductList() {
		return validProductList;
	}
	public void setValidProductList(List<ProductDto> validProductList) {
		this.validProductList = validProductList;
	}

	

}
