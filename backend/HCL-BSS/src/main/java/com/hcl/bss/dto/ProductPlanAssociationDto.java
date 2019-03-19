package com.hcl.bss.dto;

public class ProductPlanAssociationDto {
private ProductDto product;
private RatePlanProductDto ratePlan;


public ProductPlanAssociationDto() {
	super();
}


public ProductPlanAssociationDto(ProductDto product, RatePlanProductDto ratePlan) {
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


public RatePlanProductDto getRatePlan() {
	return ratePlan;
}


public void setRatePlan(RatePlanProductDto ratePlan) {
	this.ratePlan = ratePlan;
}


}
