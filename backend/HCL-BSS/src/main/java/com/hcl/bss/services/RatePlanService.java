package com.hcl.bss.services;

import java.io.Serializable;

import com.hcl.bss.dto.ProductDto;

public interface RatePlanService {

	Serializable addRatePlan(ProductDto product);

}
