package com.hcl.bss.repository;

import com.hcl.bss.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, JpaSpecificationExecutor<Subscription>, PagingAndSortingRepository<Subscription, Long> {
    /**
     * This method will get the next sequence number which will be used to generate a unique SubscriptionID
     * @return
     */
    @Query(value="SELECT getNextSeq('SubscriptionSeq')", nativeQuery = true)
    public Integer getSubsSeq();

    public Subscription findBySubscriptionId(String name);

    public List<Subscription> findByIsActive(int isActive);
    
    @Query(value="select SUBSCRIPTION_ID from tb_subscription where CRE_DT>=DATE_SUB(NOW(), INTERVAL 1 day)",nativeQuery=true)
	public List<String> getLastSubscriptionIds();
    
    @Query(value="SELECT AMOUNT from TB_SUBSCRIPTION where TRANSACTION_REASON_CODE_ID='NEW' AND ACTIVATION_DT>=?", nativeQuery = true)
    List<BigDecimal> findAmntByDate(Date date);
}