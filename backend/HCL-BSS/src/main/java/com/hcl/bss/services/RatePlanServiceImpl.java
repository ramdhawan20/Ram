package com.hcl.bss.services;

import java.io.Serializable;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hcl.bss.dto.ProductDto;
@Service
@Transactional
public class RatePlanServiceImpl implements RatePlanService {

	@Override
	public Serializable addRatePlan(ProductDto product) {
		// TODO Auto-generated method stub
		return null;
	}

}
