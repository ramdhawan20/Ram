package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.HYPHEN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.BatchLog;
import com.hcl.bss.repository.specification.BatchLogSpecification;
import com.hcl.bss.dto.BatchRunLogDto;
import com.hcl.bss.repository.AppConstantRepository;
import com.hcl.bss.repository.BatchLogRepository;

@Service
public class BatchLogServiceImpl implements BatchLogService {
	
	@Autowired
    private BatchLogRepository batchLogRepository;
	
	@Autowired
	AppConstantRepository appConstantRepository;
		
	@Override
	public List <BatchRunLogDto> findBatchOrders(Pageable reqCount,Date startDate, Date endDate, String status) {
		// TODO Auto-generated method stub
		List<BatchLog> lastBatchLogData = new ArrayList<>();
		Page<BatchLog> result= batchLogRepository.findAll(Specification.where(BatchLogSpecification.hasStartDate(startDate)).and(BatchLogSpecification.hasEndDate(endDate)).and(BatchLogSpecification.hasStatus(status)),reqCount);
		lastBatchLogData = result.getContent();
		List <BatchRunLogDto> lastRunLogDtoList = new ArrayList<>();
		lastBatchLogData.forEach(order -> { BatchRunLogDto batchRunLogDto = new
		  BatchRunLogDto(); if(null != order.getRunDownDate()) {
		  batchRunLogDto.setDate(this.getStringDate(new
		  Date(order.getRunDownDate().getTime()))); }
		  batchRunLogDto.setOrderNumber(order.getOrderNumber());
		  batchRunLogDto.setStatus(order.getStatus());
		  batchRunLogDto.setErrorDesc(order.getErrorDesc());
		  if(order.getSubscriptionId()!=null)
		  batchRunLogDto.setSubsId(order.getSubscriptionId());
		  else
		  batchRunLogDto.setSubsId(HYPHEN);
		  lastRunLogDtoList.add(batchRunLogDto);
		});
		 
		return lastRunLogDtoList;
	}
	@Override
	public long findSuccessCountByDate(Date startDate, Date endDate, String status) {
		// TODO Auto-generated method stub
		return batchLogRepository.count(Specification.where(BatchLogSpecification.hasStartDate(startDate)).and(BatchLogSpecification.hasEndDate(endDate)).and(BatchLogSpecification.hasStatus(status)));
	}
	@Override
	public long findLastFailCount(String status) {
		// TODO Auto-generated method stub
		return batchLogRepository.findLastFailCount(status);
	}
	@Override
	public long findLastSuccessCount(Date startDate, String status) {
		// TODO Auto-generated method stub
		return batchLogRepository.count(Specification.where(BatchLogSpecification.hasStartDate(startDate)).and(BatchLogSpecification.hasStatus(status)));
	}

	@Override
	public long findFailCountByDate(Date startDate, Date endDate, String status) {
		// TODO Auto-generated method stub
		if(endDate!=null)
		return batchLogRepository.findFailCount(startDate, endDate, status);
		else
		return batchLogRepository.findFailCount(startDate, status);
	}
	@Override
	public List<BatchRunLogDto> findLastBatchOrders(Pageable reqCount, Date date, String status) {
		// TODO Auto-generated method stub
		List<BatchLog> lastBatchLogData = new ArrayList<>();
		Page<BatchLog> result= batchLogRepository.findAll(Specification.where(BatchLogSpecification.hasStartDate(date)).and(BatchLogSpecification.hasStatus(status)),reqCount);
		lastBatchLogData = result.getContent();
		List <BatchRunLogDto> lastRunLogDtoList = new ArrayList<>();
		lastBatchLogData.forEach(order -> {
			  BatchRunLogDto batchRunLogDto = new BatchRunLogDto();
			  batchRunLogDto.setDate(this.getStringDate(new Date(order.getRunDownDate().getTime())));
			  batchRunLogDto.setOrderNumber(order.getOrderNumber());
			  batchRunLogDto.setStatus(order.getStatus());
			  batchRunLogDto.setErrorDesc(order.getErrorDesc());
			  batchRunLogDto.setSubsId(HYPHEN);
			  lastRunLogDtoList.add(batchRunLogDto);
		  });
		return lastRunLogDtoList;
	}
	@Override
	public long findTotalCountByDate(Date startDate, Date endDate, String status) {
		// TODO Auto-generated method stub
			return batchLogRepository.count(Specification.where(BatchLogSpecification.hasStartDate(startDate)).and(BatchLogSpecification.hasStartDate(startDate)).and(BatchLogSpecification.hasStatus(status)));
	}
	
	private String getStringDate(Date dateToFormat) {
		String formatedDate=BLANK;
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);
		formatedDate = dateFormat.format(dateToFormat);
		return formatedDate;
	}
	@Override
	public List<String> getDropDownData(String statusId) {
		try {
			return appConstantRepository.findByDropdownCode(statusId);
		}
		catch (Exception e) {
			return null;
		}
	}
}
