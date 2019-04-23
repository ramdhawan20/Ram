package com.hcl.bss.services;

import java.util.List;

import com.hcl.bss.dto.DashboardGraphDto;
import com.hcl.bss.dto.GraphRequestDto;

public interface DashBoardService {
	
	double getLastSubBatchRev();
	List<String> getDropDownData(String statusId);
	DashboardGraphDto getDashboardGraphValues(GraphRequestDto graphRequest);
}
