package com.hcl.bss.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.dto.DashboardGraphDto;
import com.hcl.bss.dto.GraphRequestDto;
import com.hcl.bss.exceptions.AuthenticationException;
import com.hcl.bss.exceptions.CustomSubscriptionException;
import com.hcl.bss.repository.AppConstantRepository;
import com.hcl.bss.repository.SubscriptionRepository;
import com.hcl.bss.repository.specification.SubscriptionSpecification;
import com.sun.org.apache.xpath.internal.operations.And;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Autowired
	SubscriptionRepository subscriptionRepository;
	@Autowired
	AppConstantRepository appConstantRepository;
	
	@Override
	public double getLastSubBatchRev() {
		
		double revenue=0;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		List<BigDecimal> amntList = subscriptionRepository.findAmntByDate(cal.getTime());
		if(amntList!=null && !amntList.isEmpty()) {
				for(BigDecimal amnt: amntList) {
				revenue = amnt.doubleValue()+revenue;
			}
			return revenue;
		}
		else
			throw new CustomSubscriptionException(105); 
	}

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
		List<Long> activValues = new ArrayList<Long>();
		List<Long> cancelValues = new ArrayList<Long>();
		List<String> timePeriod = new ArrayList<String>();
		LocalDate date1 = LocalDate.now().atTime(LocalTime.MAX).toLocalDate();
		if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Month")) {
			date1 = date1.minusMonths(1);
			date1 = date1.minusDays(date1.getDayOfMonth()-1);
			for(int j=1;j<=date1.lengthOfMonth();j++) {
				timePeriod.add(Integer.toString(j));
				activValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("ACTIVE").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
				cancelValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("CANCELLED").and(SubscriptionSpecification.hasDate(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(1);
			}
			dashboardGraphDto.setActivSubValues(activValues);
			dashboardGraphDto.setCancelSubValues(cancelValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Week")) {
			date1 = date1.minusWeeks(1);
			date1 = date1.minusDays(date1.getDayOfWeek().getValue());
			for(int j=1;j<=7;j++) {
				timePeriod.add(date1.getDayOfWeek().toString());
				activValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("ACTIVE").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
				cancelValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("CANCELLED").and(SubscriptionSpecification.hasDate(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(1);
			}
			dashboardGraphDto.setActivSubValues(activValues);
			dashboardGraphDto.setCancelSubValues(cancelValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Year")) {
			date1 = date1.minusYears(1);
			date1 = date1.minusDays(date1.getDayOfYear()-1);
			for(int j=1;j<=12;j++) {
				timePeriod.add(date1.getMonth().toString());
				activValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("ACTIVE").and(SubscriptionSpecification.hasDate(Date.from(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
//				System.out.println(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
				cancelValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("CANCELLED").and(SubscriptionSpecification.hasDate(Date.from(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(date1.lengthOfMonth());
			}
			dashboardGraphDto.setActivSubValues(activValues);
			dashboardGraphDto.setCancelSubValues(cancelValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else if (graphRequest.getTimePeriod().equalsIgnoreCase("Last Thirty Days")){
			date1 = date1.minusDays(30);
			for(int j=1;j<=30;j++) {
				timePeriod.add(Integer.toString(date1.getDayOfMonth()));
				activValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("ACTIVE").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
				System.out.println(date1);
				cancelValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("CANCELLED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(1);
			}
			dashboardGraphDto.setActivSubValues(activValues);
			dashboardGraphDto.setCancelSubValues(cancelValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else
			throw new AuthenticationException("Invalid Graph Type"); 
	}
	
	private DashboardGraphDto getNewRenewSubValuesByTimePeriod(GraphRequestDto graphRequest, DashboardGraphDto dashboardGraphDto) {
		List<Long> newValues = new ArrayList<Long>();
		List<Long> renewValues = new ArrayList<Long>();
		List<String> timePeriod = new ArrayList<String>();
		LocalDate date1 = LocalDate.now();
		if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Month")) {
			date1 = date1.minusMonths(1);
			date1 = date1.minusDays(date1.getDayOfMonth()-1);
			for(int j=1;j<=date1.lengthOfMonth();j++) {
				timePeriod.add(Integer.toString(j));
				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.isActive("RENEWED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(1);
			}
			dashboardGraphDto.setNewSubValues(newValues);
			dashboardGraphDto.setRenewedSubValues(renewValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Week")) {
			date1 = date1.minusWeeks(1);
			date1 = date1.minusDays(date1.getDayOfWeek().getValue());
			for(int j=1;j<=7;j++) {
				timePeriod.add(date1.getDayOfWeek().toString());
				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("RENEWED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(1);
			}
			dashboardGraphDto.setNewSubValues(newValues);
			dashboardGraphDto.setRenewedSubValues(renewValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else if(graphRequest.getTimePeriod().equalsIgnoreCase("Last Year")) {
			date1 = date1.minusYears(1);
			date1 = date1.minusDays(date1.getDayOfYear()-1);
			for(int j=1;j<=12;j++) {
				timePeriod.add(date1.getMonth().toString());
				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
//				System.out.println(date1.plusDays(date1.lengthOfMonth()-1).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
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
			for(int j=1;j<=30;j++) {
				timePeriod.add(Integer.toString(date1.getDayOfMonth()));
				newValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("NEW").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				System.out.println(date1);
				renewValues.add(subscriptionRepository.count(Specification.where(SubscriptionSpecification.transReasonCode("RENEWED").and(SubscriptionSpecification.hasDate(Date.from(date1.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))).and(SubscriptionSpecification.hasStrtDate(Date.from(date1.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()))))));
				date1 = date1.plusDays(1);
			}
			dashboardGraphDto.setNewSubValues(newValues);
			dashboardGraphDto.setRenewedSubValues(renewValues);
			dashboardGraphDto.setTimePeriod(timePeriod);
			return dashboardGraphDto;
		}
		else
			throw new AuthenticationException("Invalid Graph Type");
	}
}
