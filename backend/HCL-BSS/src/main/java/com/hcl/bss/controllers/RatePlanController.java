package com.hcl.bss.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanDto;
import com.hcl.bss.dto.ResponseDto;
import com.hcl.bss.services.RatePlanService;

import io.swagger.annotations.ApiOperation;
@CrossOrigin(origins = "*")
@RestController
public class RatePlanController {
		@Autowired
		RatePlanService ratePlanService;
		
		@ApiOperation(value = "Add RatePlan", response = ResponseDto.class)
		@RequestMapping(value = "/rate/ratePlan", produces = { "application/json" }, method = RequestMethod.POST)
		public ResponseEntity<ResponseDto> addRatePlan(@RequestBody RatePlanDto product) {
			ResponseDto response = new ResponseDto();
			try {
				response = ratePlanService.addRatePlan(product);
				return new ResponseEntity<ResponseDto>(response,HttpStatus.OK);
			}
			catch (DataIntegrityViolationException e) {
				// TODO: handle exception
				response.setMessage("Could not add Plan");
				return new ResponseEntity<ResponseDto>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		
		@ApiOperation(value = "Get All RatePlans", response = RatePlanDto.class)
		@RequestMapping(value = "/rate/getRatePlan", produces = { "application/json" }, method = RequestMethod.GET)
		public ResponseEntity<List<RatePlanDto>> getAllRatePlan() {
			
			List<RatePlanDto> planList = new ArrayList<RatePlanDto>();
			planList = ratePlanService.getAllPlans();
			return new ResponseEntity<>(planList, HttpStatus.OK);

		}
		
		@ApiOperation(value = "getCurrency", response = String.class)
		@RequestMapping(value = "/rate/getCurrency", produces = { "application/json" }, method = RequestMethod.GET)
		public ResponseEntity<List<String>> getCurrency() {
			List<String> currencyList=new ArrayList<String>();
			currencyList = ratePlanService.getCurrency();
			return new ResponseEntity<>(currencyList, HttpStatus.OK);
		}
}
