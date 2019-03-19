package com.hcl.bss.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DDMMYYYY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hcl.bss.dto.BatchDto;
import com.hcl.bss.dto.BatchRunLogDto;
import com.hcl.bss.dto.FilterRequest;
import com.hcl.bss.services.BatchLogService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
public class BatchController {
	
	@Autowired
	BatchLogService batchLogService;
	
	@Value("${default.recordPerPage:5}")
	Integer recordPerPage;
	
	@ApiOperation(value = "Get last subscription batch report error log", response = BatchDto.class)
	@RequestMapping(value = "/lastBatchRunLog/{pageNo}", produces = {
	  "application/json"
	 }, method = RequestMethod.GET)
	 public ResponseEntity<BatchDto> getLastBatchRunLog(@PathVariable("pageNo") String pageNo) {
	
	  BatchDto orderDto = new BatchDto();
	  Integer pageNumber = Integer.valueOf(pageNo);
	  @SuppressWarnings("deprecation")
      Pageable reqCount = new PageRequest(pageNumber, recordPerPage);
	  
	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.DATE, -1);
	  orderDto.setFailed(batchLogService.findLastFailCount(com.hcl.bss.constants.ApplicationConstants.FAIL_STATUS));
	  orderDto.setSuccess(batchLogService.findLastSuccessCount(cal.getTime(),com.hcl.bss.constants.ApplicationConstants.STATUS_SUCCESS));
	  List <BatchRunLogDto> responseList = batchLogService.findLastBatchOrders(reqCount, cal.getTime(), com.hcl.bss.constants.ApplicationConstants.FAIL_STATUS);
	  Integer pageNum = reqCount.getPageNumber()+1;
	  Integer lastRecord = pageNum * reqCount.getPageSize();
	  if((batchLogService.findTotalCountByDate(cal.getTime(),null,com.hcl.bss.constants.ApplicationConstants.FAIL_STATUS))-lastRecord <= 0) {
		  if(responseList.size()/lastRecord == 0) {
			  orderDto.setLastPage(true);
			}
		}
	  else
		  orderDto.setLastPage(false);
	  Long noOfTotalRecords = batchLogService.findTotalCountByDate(cal.getTime(),null,com.hcl.bss.constants.ApplicationConstants.FAIL_STATUS);
	  Long totalPages = noOfTotalRecords/reqCount.getPageSize();
		if(noOfTotalRecords%reqCount.getPageSize() != 0) {
			totalPages = totalPages+1;
		}
	  orderDto.setTotalPages(totalPages);
	  orderDto.setBatchRunLogDtoList(responseList);
	  cal.clear();
	  return new ResponseEntity<>(orderDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get filtered subscription batch report log", response = BatchDto.class)
	 @RequestMapping(value = "/batchRunLog", produces = {
	  "application/json"
	 }, method = RequestMethod.POST)
	 public ResponseEntity<BatchDto> getCustomBatchRunLog(@RequestBody FilterRequest filterRequest) {

	  BatchDto orderDto = new BatchDto();
	  Integer pageNumber = Integer.valueOf(filterRequest.getPageNo());
	  @SuppressWarnings("deprecation")
      Pageable reqCount = new PageRequest(pageNumber, recordPerPage);
	  Date startDate = null;
	  Date endDate = null;
	  try{
		  if(filterRequest.getStartDate()!=null && filterRequest.getEndDate()!=null) {
			  startDate = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY).parse(filterRequest.getStartDate());
			  endDate = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY).parse(filterRequest.getEndDate());
		  }
		  else
			  startDate = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY).parse(filterRequest.getStartDate());			 
	  }
	  catch (Exception e) {
		e.printStackTrace();
	}
	  orderDto.setSuccess(batchLogService.findSuccessCountByDate(startDate, endDate, com.hcl.bss.constants.ApplicationConstants.STATUS_SUCCESS));
	  orderDto.setFailed(batchLogService.findFailCountByDate(startDate, endDate, com.hcl.bss.constants.ApplicationConstants.FAIL_STATUS));
	  List <BatchRunLogDto> responseList = batchLogService.findBatchOrders(reqCount, startDate, endDate,filterRequest.getStatus());
	  Integer pageNo = reqCount.getPageNumber()+1;
	  Integer lastRecord = pageNo * reqCount.getPageSize();
	  if((batchLogService.findTotalCountByDate(startDate,endDate,filterRequest.getStatus()))-lastRecord <= 0) {
		  if(responseList.size()/lastRecord == 0) {
			  orderDto.setLastPage(true);
			}
		}
	  else
		  orderDto.setLastPage(false);
	  Long noOfTotalRecords = batchLogService.findTotalCountByDate(startDate,endDate,filterRequest.getStatus());
	  Long totalPages = noOfTotalRecords/reqCount.getPageSize();
		if(noOfTotalRecords%reqCount.getPageSize() != 0) {
			totalPages = totalPages+1;
		}
	  orderDto.setTotalPages(totalPages);
	  orderDto.setBatchRunLogDtoList(responseList);
	  return new ResponseEntity<>(orderDto,HttpStatus.OK);
	 }
}
