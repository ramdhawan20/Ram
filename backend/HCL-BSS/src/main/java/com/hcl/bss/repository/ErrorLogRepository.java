package com.hcl.bss.repository;

import com.hcl.bss.domain.BatchLog;
import com.hcl.bss.domain.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author- Aditya gupta
 */
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
