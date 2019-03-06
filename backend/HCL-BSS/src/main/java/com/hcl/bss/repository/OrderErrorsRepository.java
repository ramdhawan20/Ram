package com.hcl.bss.repository;

import com.hcl.bss.domain.OrderErrors;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface OrderErrorsRepository extends JpaRepository<OrderErrors, Long> {
}
