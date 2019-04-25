package com.hcl.bss.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.constants.ApplicationConstants.TransactionReasonCode;
import com.hcl.bss.dto.BatchRenewalDto;
import com.hcl.bss.dto.DashboardGraphDto;
import com.hcl.bss.dto.GraphRequestDto;
import com.hcl.bss.dto.RevenueDto;
import com.hcl.bss.exceptions.AuthenticationException;
import com.hcl.bss.repository.AppConstantRepository;
import com.hcl.bss.repository.RenewalBatchLogRepository;
import com.hcl.bss.repository.SubscriptionRepository;
import com.hcl.bss.repository.specification.SubscriptionSpecification;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Autowired
	SubscriptionRepository subscriptionRepository;
	@Autowired
	AppConstantRepository appConstantRepository;
	@Autowired
	RenewalBatchLogRepository renewalBatchLogRepository;
	
	@Override
	public List<String> getDropDownData(String statusId) {
			return appConstantRepository.findByAppConstantCode(statusId);
	}

	@Override
	public DashboardGraphDto getDashboardGraphValues(GraphRequestDto graphRequest) {
		// TODO Auto-generated method stub
		DashboardGraphDto dashboardGraphDto = new DashboardGraphDto();
		switch(graphRequest.getTypeOfGraph()){
    		case "type1":
//    			dashboardGraphDto.setActivCustValues(getActivCustValuesByTimePeriod(graphRequest));
    			break;
    		case "ACTIVE VS CANCEL":
    			getActivCancelSubValuesByTimePeriod(graphRequest,dashboardGraphDto);
    			break;
    		case "NEW VS RENEW":
    			getNewRenewSubValuesByTimePeriod(graphRequest,dashboardGraphDto);
    			break;
    		default :
    			throw new AuthenticationException("Invalid Graph Type");
		}
		return dashboardGraphDto;
	}
	
	
	private DashboardGraphDto getActivCancelSubValuesByTimePeriod(GraphRequestDto graphRequest, DashboardGraphDto dashboardGraphDto){

		LocalDate date1 = LocalDate.now().atTime(LocalTime.MAX).toLocalDate();
		if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Month")) {
			date1 = date1.minusMonths(1);
			date1 = date1.minusDays(date1.getDayOfMonth()-1);
			return getActivCancel(dashboardGraphDto, date1.lengthOfMonth(), date1,1);
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Week")) {
			date1 = date1.minusWeeks(1);
			date1 = date1.minusDays(date1.getDayOfWeek().getValue());
			return getActivCancel(dashboardGraphDto, 7, date1,1);
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Year")) {
			List<Long> activValues = new ArrayList<Long>();
			List<Long> cancelValues = new ArrayList<Long>();
			List<String> timePeriod = new ArrayList<String>();
			date1 = date1.minusYears(1);
			date1 = date1.minusDays(date1.getDayOfYear()-1);
			for(int j=1;j<=12;j++) {
				timePeriod.add(date1.getMonth().toString());
				activValues.add(subscriptionRepository.count(Specification.where
						(SubscriptionSpecification.hasActivDateStrt(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))).
						and(SubscriptionSpecification.hasActivDateEnd(Date.from(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant())))));
				cancelValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isStatus("CANCELLED").
						and(SubscriptionSpecification.hasCancelStrtDate(date1)).
						and(SubscriptionSpecification.hasCancelEndDate(date1.plusDays(date1.lengthOfMonth()-1))))));
				date1 = date1.plusDays(date1.lengthOfMonth());
			}
			dashboardGraphDto.setActivSubValues(activValues);
			dashboardGraphDto.setCancelSubValues(cancelValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
//			return getActivCancel(dashboardGraphDto, 12, Date.from(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()), date1, date1.lengthOfMonth());
		}
		else if (graphRequest.getTimePeriod().equalsIgnoreCase("Last Thirty Days")){
			date1 = date1.minusDays(30);
			return getActivCancel(dashboardGraphDto, 30,date1, 1);
		}
		else
			throw new AuthenticationException("Invalid Graph Type"); 
	}
	
	private DashboardGraphDto getActivCancel(DashboardGraphDto dashboardGraphDto,int k,LocalDate date1,int d) {
		List<Long> activValues = new ArrayList<Long>();
		List<Long> cancelValues = new ArrayList<Long>();
		List<String> timePeriod = new ArrayList<String>();
		for(int j=1;j<=k;j++) {
			timePeriod.add(Integer.toString(date1.getDayOfMonth()));
			activValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.hasActivDateStrt(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasActivDateEnd(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant())))));
			cancelValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isStatus("CANCELLED").and(SubscriptionSpecification.hasCancelDate(date1)))));
			date1 = date1.plusDays(d);
		}
		dashboardGraphDto.setActivSubValues(activValues);
		dashboardGraphDto.setCancelSubValues(cancelValues);
		dashboardGraphDto.setTimePeriod(timePeriod);
		return dashboardGraphDto;
	}
	
	private DashboardGraphDto getNewRenewSubValuesByTimePeriod(GraphRequestDto graphRequest, DashboardGraphDto dashboardGraphDto) {
//		List<Long> newValues = new ArrayList<Long>();
//		List<Long> renewValues = new ArrayList<Long>();
//		List<String> timePeriod = new ArrayList<String>();
		LocalDate date1 = LocalDate.now();
		if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Month")) {
			date1 = date1.minusMonths(1);
			date1 = date1.minusDays(date1.getDayOfMonth()-1);
//			for(int j=1;j<=date1.lengthOfMonth();j++) {
//				timePeriod.add(Integer.toString(j));
//				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("RENEWED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				date1 = date1.plusDays(1);
//			}
//			dashboardGraphDto.setNewSubValues(newValues);
//			dashboardGraphDto.setRenewedSubValues(renewValues);
//			dashboardGraphDto.setTimePeriod(timePeriod);
//			return dashboardGraphDto;
			return getNewRenew(dashboardGraphDto, date1.lengthOfMonth(), date1, 1);
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Week")) {
			date1 = date1.minusWeeks(1);
			date1 = date1.minusDays(date1.getDayOfWeek().getValue());
//			for(int j=1;j<=7;j++) {
//				timePeriod.add(date1.getDayOfWeek().toString());
//				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("RENEWED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				date1 = date1.plusDays(1);
//			}
//			dashboardGraphDto.setNewSubValues(newValues);
//			dashboardGraphDto.setRenewedSubValues(renewValues);
//			dashboardGraphDto.setTimePeriod(timePeriod);
//			return dashboardGraphDto;
			return getNewRenew(dashboardGraphDto, 7, date1, 1);
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Year")) {
			List<Long> newValues = new ArrayList<Long>();
			List<Long> renewValues = new ArrayList<Long>();
			List<String> timePeriod = new ArrayList<String>();
			date1 = date1.minusYears(1);
			date1 = date1.minusDays(date1.getDayOfYear()-1);
			for(int j=1;j<=12;j++) {
				timePeriod.add(date1.getMonth().toString());
				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("RENEWED").and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasDate(Date.from(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(date1.lengthOfMonth());
			}
			dashboardGraphDto.setNewSubValues(newValues);
			dashboardGraphDto.setRenewedSubValues(renewValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else if (graphRequest.getTimePeriod().equalsIgnoreCase("Last Thirty Days")){
			date1 = date1.minusDays(30);
//			for(int j=1;j<=30;j++) {
//				timePeriod.add(Integer.toString(date1.getDayOfMonth()));
//				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("RENEWED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				date1 = date1.plusDays(1);
//			}
//			dashboardGraphDto.setNewSubValues(newValues);
//			dashboardGraphDto.setRenewedSubValues(renewValues);
//			dashboardGraphDto.setTimePeriod(timePeriod);
//			return dashboardGraphDto;
			return getNewRenew(dashboardGraphDto, 30, date1, 1);
		}
		else
			throw new AuthenticationException("Invalid Graph Type");
	}
	
	private DashboardGraphDto getNewRenew(DashboardGraphDto dashboardGraphDto,int k, LocalDate date1, int d) {
		List<Long> newValues = new ArrayList<Long>();
		List<Long> renewValues = new ArrayList<Long>();
		List<String> timePeriod = new ArrayList<String>();
		for(int j=1;j<=k;j++) {
			timePeriod.add(Integer.toString(date1.getDayOfMonth()));
			newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").
					and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).
					and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
			renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isStatus("RENEWED").
					and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).
					and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
			date1 = date1.plusDays(1);
		}
		dashboardGraphDto.setNewSubValues(newValues);
		dashboardGraphDto.setRenewedSubValues(renewValues);
		dashboardGraphDto.setTimePeriod(timePeriod);
		return dashboardGraphDto;
	}
	
	@Override
	public RevenueDto getRevenue(RevenueDto revenueDto) {
		// TODO Auto-generated method stub
		LocalDate date = LocalDate.now();
		//lastBatch
		Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		List<BigDecimal> amntList = subscriptionRepository.findAmntByDate(TransactionReasonCode.NEW.toString(),startDate);
		revenueDto.setLastBatchRevOfNewSub(getAmountSum(amntList));
		amntList = subscriptionRepository.findAmntByDate(TransactionReasonCode.RENEWED.toString(),startDate);
		revenueDto.setLastBatchRevOfRenewSub(getAmountSum(amntList));
		//thisMonth
		Timestamp endDate = Timestamp.valueOf(date.atTime(LocalTime.MAX));
		startDate = Timestamp.valueOf(date.minusDays(date.getDayOfMonth()-1).atStartOfDay());
		amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.NEW.toString(),startDate, endDate);
		revenueDto.setThisMonthRevOfNewSub(getAmountSum(amntList));
		amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.RENEWED.toString(),startDate, endDate);
		revenueDto.setThisMonthRevOfRenewSub(getAmountSum(amntList));
//		Last month
		startDate = Timestamp.valueOf(date.minusMonths(1).minusDays(date.minusMonths(1).getDayOfMonth()-1).atStartOfDay());
		endDate = Timestamp.valueOf(date.minusMonths(1).minusDays(date.minusMonths(1).getDayOfMonth()-1).plusDays(date.minusMonths(1).lengthOfMonth()-1).atTime(LocalTime.MAX));amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.NEW.toString(),startDate, endDate);
		amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.NEW.toString(),startDate, endDate);
		revenueDto.setLastMonthRevOfNewSub(getAmountSum(amntList));
		amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.RENEWED.toString(),startDate, endDate);
		revenueDto.setLastMonthRevOfRenewSub(getAmountSum(amntList));
//		This Year
		startDate = Timestamp.valueOf(date.minusMonths(date.getMonthValue()-1).minusDays(date.minusMonths(date.getMonthValue()-1).getDayOfMonth()-1).atStartOfDay());
		endDate = Timestamp.valueOf(date.atTime(LocalTime.MAX));
		amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.NEW.toString(),startDate, endDate);
		revenueDto.setThisYearRevOfNewSub(getAmountSum(amntList));
		amntList = subscriptionRepository.findAmntByStartEndDate(TransactionReasonCode.RENEWED.toString(),startDate, endDate);
		revenueDto.setThisYearRevOfRenewSub(getAmountSum(amntList));
		return revenueDto;
	}
	
	private double getAmountSum(List<BigDecimal> amntList) {
		double revenue=0;
		if(amntList!=null && !amntList.isEmpty()) {
			for(BigDecimal amnt: amntList) {
				revenue = amnt.doubleValue()+revenue;
			}
			amntList=null;
		}
		return revenue;
	}

	@Override
	public BatchRenewalDto getLastBatchRenewalCount() {

		BatchRenewalDto batchRenewalDto = new BatchRenewalDto();
		Long total = renewalBatchLogRepository.getTotalCountOfLastBatch(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
		Long success = renewalBatchLogRepository.getSuccessCountLastBatch(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
		Long failed = renewalBatchLogRepository.getFailCountLastBatch(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
		batchRenewalDto.setTotal(total);
		batchRenewalDto.setSuccess(success);
		batchRenewalDto.setFailed(failed);
		return batchRenewalDto;
	}
}
