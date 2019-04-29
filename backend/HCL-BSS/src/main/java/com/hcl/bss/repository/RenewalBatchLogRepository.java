package com.hcl.bss.repository;

import com.hcl.bss.domain.RenewalBatchLog;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RenewalBatchLogRepository extends JpaRepository<RenewalBatchLog,Long> {
	@Query(value="SELECT COUNT(DISTINCT SUBSCRIPTION_ID) FROM TB_AUTORENEWAL_BATCH_LOG WHERE RUN_DWN_DATE>=?",nativeQuery=true)
	long getTotalCountOfLastBatch(Date date);

	@Query(value="SELECT COUNT(DISTINCT SUBSCRIPTION_ID) FROM TB_AUTORENEWAL_BATCH_LOG WHERE STATUS='SUCCESS' AND RUN_DWN_DATE>=?",nativeQuery=true)
	long getSuccessCountLastBatch(Date date);

	@Query(value="SELECT COUNT(DISTINCT SUBSCRIPTION_ID) FROM TB_AUTORENEWAL_BATCH_LOG WHERE STATUS='FAILURE' AND RUN_DWN_DATE>=?",nativeQuery=true)
	long getFailCountLastBatch(Date date); 
}
