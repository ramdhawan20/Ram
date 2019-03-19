package com.hcl.bss.repository;

import com.hcl.bss.domain.BatchLog;
import com.hcl.bss.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author- Aditya gupta
 */
public interface BatchLogRepository extends JpaRepository<BatchLog, Long> {

    @Query(value = "Select * from tb_batch_run_log where order_number =?1", nativeQuery = true)
    public List<BatchLog> findByOrderNumber(String orderNumber);

}
