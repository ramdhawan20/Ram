package com.hcl.bss.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.UOM;
import com.hcl.bss.dto.DropDownOutDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanDto;
import com.hcl.bss.dto.RatePlanFilterReqDto;
import com.hcl.bss.dto.RatePlanResponseDto;
import com.hcl.bss.dto.ResponseDto;
import com.hcl.bss.exceptions.CustomRatePlanException;
import com.hcl.bss.services.RatePlanService;

import io.swagger.annotations.ApiOperation;
@CrossOrigin(origins = "*")
@RestController
public class RatePlanController {
		@Autowired
		RatePlanService ratePlanService;
		
		@Value("${app.page.size}")
		Integer recordPerPage;
		
		@ApiOperation(value = "Add RatePlan", response = ResponseDto.class)
		@RequestMapping(value = "/ratePlan", produces = { "application/json" }, method = RequestMethod.POST)
		public ResponseEntity<ResponseDto> addRatePlan(@RequestBody RatePlanDto product) {
			ResponseDto response = new ResponseDto();
			try {
				response = ratePlanService.addRatePlan(product);
				return new ResponseEntity<ResponseDto>(response,HttpStatus.OK);
			}
			catch(DataIntegrityViolationException e) {
				response.setMessage("Could not add Plan "+e.getRootCause().getMessage());
				return new ResponseEntity<ResponseDto>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		
		@ApiOperation(value = "Get RatePlans", response = RatePlanDto.class)
		@RequestMapping(value = "/getRatePlan", produces = { "application/json" }, method = RequestMethod.POST)
		public ResponseEntity<RatePlanResponseDto> getAllRatePlan(@RequestBody RatePlanFilterReqDto filterReqDto) {
			
			Integer pageNumber = Integer.valueOf(filterReqDto.getPageNo());
			Pageable reqCount = PageRequest.of(pageNumber, recordPerPage);
			RatePlanResponseDto responseDto = new RatePlanResponseDto();
			responseDto.setRatePlanList( ratePlanService.getRatePlans(reqCount,filterReqDto));
			if(responseDto.getRatePlanList()!=null && !responseDto.getRatePlanList().isEmpty()) {
				Integer pageNum = reqCount.getPageNumber()+1;
				Long noOfTotalRecords = ratePlanService.getTotalNumberOfRatePlan(filterReqDto);
				if(noOfTotalRecords>pageNum*reqCount.getPageSize())
					responseDto.setLastPage(false);
				else
					responseDto.setLastPage(true);
				Long totalPages = noOfTotalRecords/reqCount.getPageSize();
				if(noOfTotalRecords%reqCount.getPageSize() != 0) {
					totalPages = totalPages+1;
				}
				responseDto.setTotalPages(totalPages);
				return new ResponseEntity<>(responseDto, HttpStatus.OK);
			}
			throw new CustomRatePlanException(100,"No Rate Plan Found");
		}
		
		@ApiOperation(value = "getCurrency", response = String.class)
		@RequestMapping(value = "/getCurrency", produces = { "application/json" }, method = RequestMethod.GET)
		public ResponseEntity<List<String>> getCurrency() {
			List<String> currencyList=new ArrayList<String>();
			try{
				currencyList = ratePlanService.getCurrency();
				if(!currencyList.isEmpty())
					return new ResponseEntity<>(currencyList, HttpStatus.OK);
				else
					return new ResponseEntity<>(currencyList, HttpStatus.NOT_FOUND);
			}catch (Exception e) {
				// TODO: handle exception
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@ApiOperation(value = "Get UOM", response = UOM.class)
		@RequestMapping(value = "/getUOM", produces = { "application/json" }, method = RequestMethod.GET)
		public ResponseEntity<Iterable<UOM>> getUom() {
			Iterable<UOM> uomList = new ArrayList<>();
			
			try{
				uomList = ratePlanService.getUom();
				if(uomList!=null)
				return new ResponseEntity<>(uomList, HttpStatus.OK);
				else
				return new ResponseEntity<>(uomList, HttpStatus.NOT_FOUND);
			}
			catch (Exception e) {
				// TODO: handle exception
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@ApiOperation(value = "Get Dropdown Data", response = String.class)
		@RequestMapping(value = "/getRateplanDropDown",method = RequestMethod.GET)
		public ResponseEntity<DropDownOutDto> dropDownData(@RequestParam String statusId) {
			DropDownOutDto dropDownOutDto = new DropDownOutDto();
			try {
				if(ratePlanService.getDropDownData(statusId)!=null && !(ratePlanService.getDropDownData(statusId).isEmpty())) {
					dropDownOutDto.setMessage("Drop Down Fetched Successfully");
					dropDownOutDto.setResponseCode(HttpStatus.OK.value());
					dropDownOutDto.setSuccess(true);
					dropDownOutDto.setDropDownList(ratePlanService.getDropDownData(statusId));
					return new ResponseEntity<DropDownOutDto>(dropDownOutDto,HttpStatus.OK);
				}		
				else {
					dropDownOutDto.setMessage("Drop Down values not found in Database");
					dropDownOutDto.setResponseCode(HttpStatus.NOT_FOUND.value());
					dropDownOutDto.setSuccess(false);
					return new ResponseEntity<DropDownOutDto>(dropDownOutDto,HttpStatus.NOT_FOUND);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				dropDownOutDto.setMessage(e.getMessage());
				dropDownOutDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				dropDownOutDto.setSuccess(false);
				return new ResponseEntity<DropDownOutDto>(dropDownOutDto,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
}
