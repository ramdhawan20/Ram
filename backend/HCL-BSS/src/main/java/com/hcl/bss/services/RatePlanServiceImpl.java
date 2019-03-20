package com.hcl.bss.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.RatePlan;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanProductDto;
import com.hcl.bss.repository.RatePlanRepository;
@Service
@Transactional
public class RatePlanServiceImpl implements RatePlanService {

	@Autowired
	RatePlanRepository ratePlanRepository;
	@Override
	public Serializable addRatePlan(ProductDto product) {
		
		return null;
	}

	@Override
	public List<RatePlanProductDto> getAllPlans() {
		List<RatePlan> ratePlanList = new ArrayList<>();
		ratePlanList = ratePlanRepository.findAll();
		return convertRatePlanEntityToDto(ratePlanList);
		
	}

	private List<RatePlanProductDto> convertRatePlanEntityToDto(List<RatePlan> ratePlanList) {
		List<RatePlanProductDto> ratePlanDtoList = new ArrayList<RatePlanProductDto>();
		for(RatePlan rplan : ratePlanList) {
			RatePlanProductDto rpDto = new RatePlanProductDto();
			rpDto.setBillEvery(rplan.getFrequencyCode());
			if(rplan.getIsActive() == 0) {
				rpDto.setIsActive("Inactive");
			}
			else {
				rpDto.setIsActive("Active");
			}
			rpDto.setName(rplan.getRatePlanDescription());
			rpDto.setRatePlanId(rplan.getRatePlanId());
			rpDto.setUidpk(rplan.getId());
			rpDto.setUnitOfMesureId(rplan.getUom().getUnitOfMeasure());
			rpDto.setPrice(rplan.getPrice());
			ratePlanDtoList.add(rpDto);
		}
		return ratePlanDtoList;
	}

}
