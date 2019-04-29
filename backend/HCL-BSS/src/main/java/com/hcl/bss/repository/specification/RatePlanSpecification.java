package com.hcl.bss.repository.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.hcl.bss.domain.RatePlan;
import com.hcl.bss.exceptions.CustomRatePlanException;

public class RatePlanSpecification {
	
	public static Specification<RatePlan> hasName(String planName){
		if(planName!=null)
			return (filter,cq,cb)->cb.equal(filter.get("ratePlanDescription"), planName);
		return null;	
	}
	
	public static Specification<RatePlan> hasPlanCode(String planCode){
		if(planCode!=null)
			return(filter,cq,cb)->cb.equal(filter.get("ratePlanId"), planCode);
		return null;				
	}
	
	public static Specification<RatePlan> hasBillCycleTerm(BigDecimal billCycleTerm){
		if(billCycleTerm!=null)
			return (filter,cq,cb)->cb.equal(filter.get("billingCycleTerm"), billCycleTerm);
		return null;
	}
	public static Specification<RatePlan> hasBillFreq(String billFreq){
		if(billFreq!=null)
			return (filter,cq,cb)->cb.equal(filter.get("billingFrequency"), billFreq);
		return null;
	}
	public static Specification<RatePlan> hasStatus(String status){
		if(status!=null && status.equalsIgnoreCase("ACTIVE"))
			return (filter,cq,cb)->cb.equal(filter.get("isActive"), 1);
		else if(status!=null && status.equalsIgnoreCase("INACTIVE"))
			return (filter,cq,cb)->cb.equal(filter.get("isActive"), 0);
		else if(status!=null && (!status.equalsIgnoreCase("ACTIVE") || !status.equalsIgnoreCase("INACTIVE")))
			throw new CustomRatePlanException(200,"Invalid Status");
		else
			return null;
	}
	public static Specification<RatePlan> hasType(String planType){
		if(planType!=null)
			return (filter,cq,cb)->cb.equal(filter.get("pricingScheme"), planType);
		return null;
	}
}
