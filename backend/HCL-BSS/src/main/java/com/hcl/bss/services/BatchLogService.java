package com.hcl.bss.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import com.hcl.bss.dto.BatchRunLogDto;

public interface BatchLogService {

	List <BatchRunLogDto> findBatchOrders(Pageable reqCount, Date startDate, Date endDate, String status);

	long findSuccessCountByDate(Date startDate, Date endDate, String status);

	long findLastFailCount(String status);
	
	long findLastSuccessCount(Date date, String status);

	List <BatchRunLogDto> findLastBatchOrders(Pageable reqCount, Date date, String status);

	long findFailCountByDate(Date startDate, Date endDate, String string);
	
	long findTotalCountByDate(Date startDate, Date endDate, String string);

	List<String> getDropDownData(String statusId);
}
