package com.hcl.bss.services;

import java.io.Serializable;
import java.util.List;

import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanDto;
import com.hcl.bss.dto.ResponseDto;

public interface RatePlanService {

	ResponseDto addRatePlan(RatePlanDto ratePlanDto);
	List<RatePlanDto> getAllPlans();
	List<String> getCurrency();
	Iterable<UOM> getUom();
	List<String> getDropDownData(String statusId);
}
