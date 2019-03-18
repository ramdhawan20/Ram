package com.hcl.bss.dto;

import java.util.List;

public class ProductDataDto {
private List<ProductDto> productList;
private boolean isLastPage;
private Long totalPages;
public List<ProductDto> getProductList() {
	return productList;
}
public void setProductList(List<ProductDto> productList) {
	this.productList = productList;
}
public boolean isLastPage() {
	return isLastPage;
}
public void setLastPage(boolean isLastPage) {
	this.isLastPage = isLastPage;
}
public Long getTotalPages() {
	return totalPages;
}
public void setTotalPages(Long totalPages) {
	this.totalPages = totalPages;
}

}
