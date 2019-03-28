package com.hcl.bss.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanDto;
import com.hcl.bss.services.RatePlanService;

import io.swagger.annotations.ApiOperation;
@CrossOrigin(origins = "*")
@RestController
public class RatePlanController {
		@Autowired
		RatePlanService ratePlanService;
		@ApiOperation(value = "Add RatePlan", response = ProductDto.class)
	    @RequestMapping(value = "/ratePlan", produces = { "application/json" }, method = RequestMethod.POST)
		public ResponseEntity<Serializable> addRatePlan(@RequestBody ProductDto product) {
			Serializable productId = ratePlanService.addRatePlan(product);
			return new ResponseEntity<>(productId, HttpStatus.OK);
			
		}
		@ApiOperation(value = "Get All RatePlans", response = RatePlanDto.class)
		@RequestMapping(value = "/getRatePlan", produces = { "application/json" }, method = RequestMethod.GET)
		public ResponseEntity<List<RatePlanDto>> getAllRatePlan() {
			
			List<RatePlanDto> planList = new ArrayList<RatePlanDto>();
			planList = ratePlanService.getAllPlans();
			return new ResponseEntity<>(planList, HttpStatus.OK);

		}
}
