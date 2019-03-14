package com.hcl.bss.repository;

import com.hcl.bss.domain.Subscription;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, JpaSpecificationExecutor<Subscription>, PagingAndSortingRepository<Subscription, Long>  {

    /**
     * This method will get the next sequence number which will be used to generate a unique SubscriptionID
     * @return
     */
    @Query(value="SELECT getNextSeq('SubscriptionSeq')", nativeQuery = true)
    public Integer getSubsSeq();
    
}
