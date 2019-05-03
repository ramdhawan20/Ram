package com.hcl.bss.dto;

public class RatePlanVolumeDto {
	
	long id;
	int startQty;
	int endQty;
	double price;
	
	public int getStartQty() {
		return startQty;
	}
	public void setStartQty(int startQty) {
		this.startQty = startQty;
	}
	public int getEndQty() {
		return endQty;
	}
	public void setEndQty(int endQty) {
		this.endQty = endQty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}



