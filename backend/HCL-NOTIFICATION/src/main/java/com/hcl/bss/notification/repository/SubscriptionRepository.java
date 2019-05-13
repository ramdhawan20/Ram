package com.hcl.bss.notification.repository;

import com.hcl.bss.notification.domain.Subscription;
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
    
    @Query(value="SELECT AMOUNT from TB_SUBSCRIPTION where TRANSACTION_REASON_CODE_ID=?1 AND CRE_DT>=?2", nativeQuery = true)
    List<BigDecimal> findAmntByDate(String newOrRenew,Date date);
    
    @Query(value="SELECT AMOUNT from TB_SUBSCRIPTION where TRANSACTION_REASON_CODE_ID=?1 AND CRE_DT>=?2 AND CRE_DT<=?3", nativeQuery = true)
    List<BigDecimal> findAmntByStartEndDate(String newOrRenew, Date startDate, Date endDate);
}