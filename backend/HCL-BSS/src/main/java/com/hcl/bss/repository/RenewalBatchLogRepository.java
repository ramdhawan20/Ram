package com.hcl.bss.repository;

import com.hcl.bss.domain.RenewalBatchLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RenewalBatchLogRepository extends JpaRepository<RenewalBatchLog,Long> {
}
