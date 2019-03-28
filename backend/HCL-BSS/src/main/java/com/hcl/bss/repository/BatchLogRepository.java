package com.hcl.bss.repository;

import com.hcl.bss.domain.BatchLog;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface BatchLogRepository extends JpaRepository<BatchLog, Long>, JpaSpecificationExecutor<BatchLog>,PagingAndSortingRepository<BatchLog, Long> {

	@Query(value = "Select * from tb_batch_run_log where order_number =?1", nativeQuery = true)
    public List<BatchLog> findByOrderNumber(String orderNumber);
	
	@Query(value="select count(A.ORDERNO) from (select count(ORDER_NUMBER) as Subscription_count,ORDER_NUMBER as ORDERNO from tb_batch_run_log where STATUS=?1 and RUN_DWN_DATE>=DATE_SUB(NOW(), INTERVAL 1 day) group by ORDER_NUMBER) as A",nativeQuery=true)
	long findLastFailCount(String status);

	@Query(value="select count(A.ORDERNO) from (select count(ORDER_NUMBER) as Subscription_count,ORDER_NUMBER as ORDERNO from tb_batch_run_log where RUN_DWN_DATE>=?1 and RUN_DWN_DATE<=?2 and STATUS=?3 group by ORDER_NUMBER,RUN_DWN_DATE) as A",nativeQuery=true)
	long findFailCount(Date startDate, Date endDate, String status);
	
	@Query(value="select count(A.ORDERNO) from (select count(ORDER_NUMBER) as Subscription_count,ORDER_NUMBER as ORDERNO from tb_batch_run_log where RUN_DWN_DATE>=?1 and STATUS=?2 group by ORDER_NUMBER,RUN_DWN_DATE) as A",nativeQuery=true)
	long findFailCount(Date startDate, String status);
}
