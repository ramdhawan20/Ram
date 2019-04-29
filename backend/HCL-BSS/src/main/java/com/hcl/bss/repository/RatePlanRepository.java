package com.hcl.bss.repository;

import com.hcl.bss.domain.RatePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author- Aditya gupta
 */
public interface RatePlanRepository extends JpaRepository<RatePlan, Long>, JpaSpecificationExecutor<RatePlan>,PagingAndSortingRepository<RatePlan, Long> {
}
