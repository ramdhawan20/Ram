package com.hcl.bss.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hcl.bss.domain.UOM;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanDto;
import com.hcl.bss.dto.RatePlanFilterReqDto;
import com.hcl.bss.dto.ResponseDto;

public interface RatePlanService {

	ResponseDto addRatePlan(RatePlanDto ratePlanDto);
	List<RatePlanDto> getRatePlans(Pageable reqCount,RatePlanFilterReqDto dto);
	List<String> getCurrency();
	Iterable<UOM> getUom();
	List<String> getDropDownData(String statusId);
	Long getTotalNumberOfRatePlan(RatePlanFilterReqDto filterReqDto);
}
