package com.hcl.bss.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hcl.bss.dto.BatchRenewalDto;
import com.hcl.bss.dto.DashboardGraphDto;
import com.hcl.bss.dto.GraphRequestDto;
import com.hcl.bss.dto.RevenueDto;

public interface DashBoardService {
	
//	double getLastSubBatchRev();
	List<String> getDropDownData(String statusId);
	DashboardGraphDto getDashboardGraphValues(GraphRequestDto graphRequest);
	RevenueDto getRevenue(RevenueDto revenueDto);
	BatchRenewalDto getLastBatchRenewalCount();
}
