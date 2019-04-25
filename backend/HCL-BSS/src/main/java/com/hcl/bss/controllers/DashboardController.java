package com.hcl.bss.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.dto.BatchRenewalDto;
import com.hcl.bss.dto.DashboardGraphDto;
import com.hcl.bss.dto.DropDownOutDto;
import com.hcl.bss.dto.GraphRequestDto;
import com.hcl.bss.dto.RevenueDto;
import com.hcl.bss.exceptions.AuthenticationException;
import com.hcl.bss.services.DashBoardService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
public class DashboardController {
	
	@Autowired
	DashBoardService dashBoardService;
	
	@ApiOperation(value="Get revenue of last subscription batch run")
	@RequestMapping(value="/lastSubBatchRev",produces = {"application/json"}, method = RequestMethod.GET)
	public ResponseEntity<RevenueDto> getRevenue() {
		RevenueDto revenueDto = new RevenueDto();
		return new ResponseEntity<>(dashBoardService.getRevenue(revenueDto),HttpStatus.OK);
	}
	
	@ApiOperation(value="Get count for renewal of last batch")
	@RequestMapping(value="/getLastBatchRenewalCount", produces= {"application/json"},method = RequestMethod.GET)
	public ResponseEntity<BatchRenewalDto> getLastBatchRenewCount(){
		return new ResponseEntity<>(dashBoardService.getLastBatchRenewalCount(),HttpStatus.OK);
	} 
	
	@ApiOperation(value="Get active customer's number based on time period")
	@RequestMapping(value="/getValuesForGraph",produces = {"application/json"}, method = RequestMethod.POST)
	public ResponseEntity<DashboardGraphDto> getGraphValues(@RequestBody GraphRequestDto graphRequest) {
		
		DashboardGraphDto dashboardGraphDto = new DashboardGraphDto();
		dashboardGraphDto = dashBoardService.getDashboardGraphValues(graphRequest);
		return new ResponseEntity<>(dashboardGraphDto,HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Dropdown Data", response = DropDownOutDto.class)
	@RequestMapping(value = "/getDashboardDropDown",method = RequestMethod.GET)
	public ResponseEntity<DropDownOutDto> dropDownData(@RequestParam String statusId) {
		DropDownOutDto dropDownOutDto = new DropDownOutDto();
		try {
			List<String> dropDownList = dashBoardService.getDropDownData(statusId);
			if(dropDownList!=null && !dropDownList.isEmpty()) {
				dropDownOutDto.setMessage("Drop Down Fetched Successfully");
				dropDownOutDto.setResponseCode(HttpStatus.OK.value());
				dropDownOutDto.setSuccess(true);
				dropDownOutDto.setDropDownList(dropDownList);
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
