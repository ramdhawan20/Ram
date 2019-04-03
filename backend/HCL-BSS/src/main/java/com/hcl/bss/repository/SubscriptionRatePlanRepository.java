package com.hcl.bss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.SubscriptionRatePlan;

/**
*
* @author- Akash Bajpai
*/
public interface SubscriptionRatePlanRepository extends JpaRepository<SubscriptionRatePlan,Long>,JpaSpecificationExecutor<SubscriptionRatePlan>{

}
