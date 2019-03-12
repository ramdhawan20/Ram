package com.hcl.bss.repository;

import com.hcl.bss.domain.BatchLog;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface BatchLogRepository extends JpaRepository<BatchLog, Long> {
}
