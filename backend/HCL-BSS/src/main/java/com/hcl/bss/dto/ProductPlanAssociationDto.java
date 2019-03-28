package com.hcl.bss.dto;

import java.util.List;

public class ProductPlanAssociationDto {
private ProductDto product;
private List<RatePlanDto> ratePlan;


public ProductPlanAssociationDto() {
	super();
}


public ProductPlanAssociationDto(ProductDto product, List<RatePlanDto> ratePlan) {
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


public List<RatePlanDto> getRatePlan() {
	return ratePlan;
}


public void setRatePlan(List<RatePlanDto> ratePlan) {
	this.ratePlan = ratePlan;
}


}
