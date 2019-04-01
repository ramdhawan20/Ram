package com.hcl.bss.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.RatePlan;
import com.hcl.bss.domain.RatePlanVolume;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanDto;
import com.hcl.bss.dto.RatePlanVolumeDto;
import com.hcl.bss.dto.ResponseDto;
import com.hcl.bss.repository.CurrencyMasterRepository;
import com.hcl.bss.repository.RatePlanRepository;
@Service
@Transactional
public class RatePlanServiceImpl implements RatePlanService {

	@Autowired
	RatePlanRepository ratePlanRepository;
	@Autowired
	CurrencyMasterRepository currencyMasterRepository;
	
	@Override
	public ResponseDto addRatePlan(RatePlanDto ratePlanDto) {
		
		ResponseDto responseDto = new ResponseDto();
		try {
			ratePlanRepository.save(convertRatePlanDtoToEntity(ratePlanDto));
			responseDto.setResponseCode(HttpStatus.OK.value());
			responseDto.setResponseStatus("Success");
			responseDto.setMessage("Plan Added Successfully");
			return responseDto;
		}catch (Exception e) {
			// TODO: handle exception
			responseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseDto.setResponseStatus("Fail");
			responseDto.setMessage("Plan Could not be added");
			return responseDto;
		}
		
	}
	
	
	
	@Override
	public List<RatePlanDto> getAllPlans() {
		List<RatePlan> ratePlanList = new ArrayList<>();
		ratePlanList = ratePlanRepository.findAll();
		return convertRatePlanEntityToDto(ratePlanList);
		
	}
	
	private List<RatePlanDto> convertRatePlanEntityToDto(List<RatePlan> ratePlanList) {
		List<RatePlanDto> ratePlanDtoList = new ArrayList<RatePlanDto>();
		for(RatePlan rplan : ratePlanList) {

			RatePlanDto rpDto = new RatePlanDto();
			rpDto.setRatePlanId(rplan.getRatePlanId());
			rpDto.setName(rplan.getRatePlanDescription());
			rpDto.setBillEvery(rplan.getBillingCycleTerm().toString()+rplan.getBillingFrequency());
			rpDto.setPricingScheme(rplan.getPricingScheme());
			rpDto.setUnitOfMesureId(rplan.getUom());
			rpDto.setPrice(rplan.getPrice());
			if(rplan.getIsActive() == 0) {
				rpDto.setIsActive("Inactive");
			}
			else {
				rpDto.setIsActive("Active");
			}
			rpDto.setUidpk(rplan.getId());
			ratePlanDtoList.add(rpDto);
		}
		return ratePlanDtoList;
	}
	
private RatePlan convertRatePlanDtoToEntity(RatePlanDto ratePlanDto) {
		
		RatePlan ratePlan = new RatePlan();
		ratePlan.setRatePlanDescription(ratePlanDto.getName());
		ratePlan.setRatePlanId(ratePlanDto.getRatePlanId());
		ratePlan.setBillingCycleTerm(ratePlanDto.getBillingCycleTerm());
		ratePlan.setBillingFrequency(ratePlanDto.getBillEvery());
		ratePlan.setPricingScheme(ratePlanDto.getPricingScheme());
		ratePlan.setFreeTrail(ratePlanDto.getFreeTrail());
		ratePlan.setSetUpFee(ratePlanDto.getSetUpFee());
		//To be decide
		if(ratePlanDto.getCurrencyCode()!=null)
			ratePlan.setCurrency(currencyMasterRepository.getOne(ratePlanDto.getCurrencyCode()));
		if(ratePlanDto.getPricingScheme().equals("UNIT")) {
			ratePlan.setPrice(ratePlanDto.getPrice());
		}
		else if(ratePlanDto.getPricingScheme().equals("VOLUME")) {
//			ratePlan.setPrice();
			ratePlan.setRatePlanVolume(convertRatePlanVolumeDtoToEntity(ratePlanDto.getRatePlanVolumeDtoList(), ratePlan));
		}
		
		ratePlan.setUom(ratePlanDto.getUnitOfMesureId());
		
		if(ratePlanDto.getIsActive().equals("INACTIVE"))
			ratePlan.setIsActive(0);
		else
			ratePlan.setIsActive(1);

		if(ratePlanDto.getExpireAfter()!=BigDecimal.valueOf(9999) && ratePlanDto.getExpireAfter()!=null)
			ratePlan.setExpireAfter(ratePlanDto.getExpireAfter());
		else
			ratePlan.setExpireAfter(BigDecimal.valueOf(9999));
		
		return ratePlan;
	}

	private List<RatePlanVolume> convertRatePlanVolumeDtoToEntity(List<RatePlanVolumeDto> ratePlanVolumeDtoList, RatePlan ratePlan){
	
		List<RatePlanVolume> ratePlanVolumeList = new ArrayList<RatePlanVolume>();
		ratePlanVolumeDtoList.forEach(ratePlanVolumeDto->{
			RatePlanVolume ratePlanVolume = new RatePlanVolume();
			ratePlanVolume.setStartQty(ratePlanVolumeDto.getStartQty());
			ratePlanVolume.setEndQty(ratePlanVolumeDto.getEndQty());
			ratePlanVolume.setPrice(ratePlanVolumeDto.getPrice());
			ratePlanVolume.setRatePlanUid(ratePlan);
			ratePlanVolumeList.add(ratePlanVolume);
		});
		return ratePlanVolumeList;
	}
	
	@Override
	public List<String> getCurrency() {
		
		List<String> currencyList = new ArrayList<String>();
		try{
			currencyList = currencyMasterRepository.getCurrency();
			return currencyList;
		}catch (Exception e) {
			// TODO: handle exception
			currencyList.add("Currency code could not be fetch");
			return currencyList;
		}
	}
}
