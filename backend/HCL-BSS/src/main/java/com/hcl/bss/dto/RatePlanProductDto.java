package com.hcl.bss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RatePlanProductDto {
private Long uidpk;
private String ratePlanId;
private String name;
private Double price;
@JsonProperty("type")
private String unitOfMesureId;
private String isActive;
private String billEvery;
public Long getUidpk() {
	return uidpk;
}
public void setUidpk(Long uidpk) {
	this.uidpk = uidpk;
}
public String getRatePlanId() {
	return ratePlanId;
}
public void setRatePlanId(String ratePlanId) {
	this.ratePlanId = ratePlanId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Double getPrice() {
	return price;
}
public void setPrice(Double price) {
	this.price = price;
}
public String getUnitOfMesureId() {
	return unitOfMesureId;
}
public void setUnitOfMesureId(String unitOfMesureId) {
	this.unitOfMesureId = unitOfMesureId;
}
public String getIsActive() {
	return isActive;
}
public void setIsActive(String isActive) {
	this.isActive = isActive;
}
public String getBillEvery() {
	return billEvery;
}
public void setBillEvery(String billEvery) {
	this.billEvery = billEvery;
}

}
