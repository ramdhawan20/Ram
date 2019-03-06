package com.hcl.bss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hcl.bss.domain.Subscription1;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepository1 extends JpaRepository<Subscription1, Long> {

    /*This method will get the subscription based on filter criteria*/
	@Query(value = "SELECT subscription.UIDPK AS id, subscription.SUBSCRIPTION_ID AS subscriptionId, subscription.STATUS AS status, subscription.CRE_DT AS createdDate, \r\n" + 
			"	subscription.ACTIVATION_DT AS activatedDate, subscription.SUBSCRIPTION_START_DT AS lastBillDate, \r\n" + 
			"	subscription.SUBSCRIPTION_END_DT AS nextBillDate, ratePlanTransaction.PRICE AS price, ratePlanMaster.RATEPLAN_DESC AS planName, \r\n" + 
			"	customer.CUST_FIRST_NAME AS customerName, customer.CUST_EMAIL AS email FROM TB_SUBSCRIPTION subscription \r\n" + 
			"	INNER JOIN TB_RATEPLAN_TRANSACTION ratePlanTransaction ON ratePlanTransaction.SUBSCRIPTION_UID = subscription.UIDPK \r\n" + 
			"	INNER JOIN TB_RATEPLAN_MASTER ratePlanMaster ON ratePlanMaster.UIDPK =ratePlanTransaction.RATE_PLAN_UID \r\n" + 
			"	INNER JOIN TB_CUSTOMER customer ON customer.UIDPK = subscription.CUST_ID \r\n" + 
			"    WHERE 1=1 and (:subscriptionId is null OR subscription.SUBSCRIPTION_ID = :subscriptionId) \r\n" + 
			"    and (:customerName is null OR subscription.SUBSCRIPTION_ID = :customerName)\r\n" + 
			"    and (:email is null OR subscription.SUBSCRIPTION_ID = :email)\r\n" + 
			"    ORDER BY subscription.CRE_DT DESC", nativeQuery = true)	
	public List<Subscription1> findAll(@Param("subscriptionId") String subscriptionId, @Param("customerName") String customerName, @Param("email") String email);
	
}
