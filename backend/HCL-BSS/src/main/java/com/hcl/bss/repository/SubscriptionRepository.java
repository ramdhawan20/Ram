package com.hcl.bss.repository;

import com.hcl.bss.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * This method will get the next sequence number which will be used to generate a unique SubscriptionID
     * @return
     */
    @Query(value="SELECT getNextSeq('SubscriptionSeq')", nativeQuery = true)
    public Integer getSubsSeq();
}
