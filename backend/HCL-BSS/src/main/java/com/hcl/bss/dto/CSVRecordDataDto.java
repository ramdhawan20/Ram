package com.hcl.bss.dto;

public class CSVRecordDataDto {
private ProductDto productData;
private SubscriptionInOut subscriptionData;
private String pageName;

public ProductDto getProductData() {
	return productData;
}

public void setProductData(ProductDto productData) {
	this.productData = productData;
}

public SubscriptionInOut getSubscriptionData() {
	return subscriptionData;
}

public void setSubscriptionData(SubscriptionInOut subscriptionData) {
	this.subscriptionData = subscriptionData;
}

public String getPageName() {
	return pageName;
}

public void setPageName(String pageName) {
	this.pageName = pageName;
}

}
