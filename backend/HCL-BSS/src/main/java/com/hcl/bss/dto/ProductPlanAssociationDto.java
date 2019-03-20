package com.hcl.bss.dto;

import java.util.List;

public class ProductPlanAssociationDto {
private ProductDto product;
private List<RatePlanProductDto> ratePlan;


public ProductPlanAssociationDto() {
	super();
}


public ProductPlanAssociationDto(ProductDto product, List<RatePlanProductDto> ratePlan) {
	super();
	this.product = product;
	this.ratePlan = ratePlan;
}


public ProductDto getProduct() {
	return product;
}


public void setProduct(ProductDto product) {
	this.product = product;
}


public List<RatePlanProductDto> getRatePlan() {
	return ratePlan;
}


public void setRatePlan(List<RatePlanProductDto> ratePlan) {
	this.ratePlan = ratePlan;
}


}
