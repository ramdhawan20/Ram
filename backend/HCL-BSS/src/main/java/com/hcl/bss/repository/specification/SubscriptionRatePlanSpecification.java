package com.hcl.bss.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.hcl.bss.domain.SubscriptionRatePlan;

public class SubscriptionRatePlanSpecification {

	private SubscriptionRatePlan filter;

	public SubscriptionRatePlanSpecification(SubscriptionRatePlan filter) {
		super();
		this.filter = filter;
	}
	
	public static Specification<SubscriptionRatePlan> hasProductUID(Long productUID){
		if(productUID!=null) {
			return (filter,cq,cb)->cb.equal(filter.get("product"), productUID);
		}
		return null;
	}
	public static Specification<SubscriptionRatePlan> hasRatePlanUID(Long ratePlanUID){
		if(ratePlanUID!=null) {
			return (filter,cq,cb)->cb.equal(filter.get("ratePlan").get("id"), ratePlanUID);
		}
		return null;
	}
}
