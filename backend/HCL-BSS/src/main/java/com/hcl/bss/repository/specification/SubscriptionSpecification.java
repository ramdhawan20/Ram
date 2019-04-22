package com.hcl.bss.repository.specification;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.Expression;

import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.SubscriptionRatePlan;
/**
 * This is SubscriptionSpecification is used to apply filter criteria on subscription
 *
 * @author- Vinay Panwar
 */
public class SubscriptionSpecification {
	
	private SubscriptionSpecification() {}

//root-->Entity(i.e. Subscription) cq --> CriteriaQuery , cb--> CriteriaBuilder

/*	public static Specification<Subscription> hasSubscriptionId(String subscriptionId){
		return (root, cq, cb)-> cb.equal(root.get("subscriptionId"), subscriptionId);
	}
*/
	public static Specification<Subscription> hasSubscriptionId(String searchTerm){
		return (root, query, cb) -> {
			String containsLikePattern = getContainsLikePattern(searchTerm);
			return cb.like(cb.lower(root.get("subscriptionId")), containsLikePattern);
		};

	}

	public static Specification<Subscription> hasStatus(String status){
		return (root, cq, cb)-> cb.equal(root.get("status"), status);
	}

	public static Specification<Subscription> hasStartDate(Date fromDate, Date toDate){
		return (root, cq, cb)-> {
			Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("createdDate").as(java.sql.Date.class),fromDate);
			Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("createdDate").as(java.sql.Date.class),toDate);
			Predicate filter = cb.and(startDatePredicate, endDatePredicate);
			return filter;
		};
	}
	
	
	public static Specification<Subscription> hasPlanName(String searchTerm) {
		return (root, query, cb) -> {
			String containsLikePattern = getContainsLikePattern(searchTerm);
			//new ArrayList<>().
			//new String().
			//return cb.or(cb.like(cb.lower(root.get("subscriptionRatePlan").get("ratePlan").get("ratePlanDescription")), containsLikePattern));
			//Expression<Set<SubscriptionRatePlan>> subsRatePlanSet = root.getModel().getSet("subscriptionRatePlan");

			return cb.like(cb.lower(root.get("subscriptionRatePlan").get("ratePlan").get("ratePlanDescription")), containsLikePattern);
		};
	}
	 
	public static Specification<Subscription> hasCustomerName(String searchTerm) {
		return (root, query, cb) -> {
			String containsLikePattern = getContainsLikePattern(searchTerm);
			//return cb.or(cb.like(cb.lower(root.get("subscriptionRatePlan").get("ratePlan").get("ratePlanDescription")), containsLikePattern));
			return cb.like(cb.lower(root.get("customer").get("firstName")), containsLikePattern);
		};
	}

	private static String getContainsLikePattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		} else {
			return "%" + searchTerm.toLowerCase() + "%";
		}
	}
	
	public static Specification<Subscription> hasStrtDate(Date startDate){
		if(startDate!=null) {
			return (filter,cq,cb)->cb.greaterThanOrEqualTo(filter.get("createdDate"), startDate);
		}
		return null;
	}
	
	public static Specification<Subscription> isActive(String activ){
		if(activ!=null) {
			return (filter,cq,cb)->cb.equal(filter.get("status"), activ);
		}
		return null;
	}
	
	public static Specification<Subscription> transReasonCode(String code){
		if(code!=null) {
			return (filter,cq,cb)->cb.equal(filter.get("transactionReasonCode"), code);
		}
		return null;
	}
	public static Specification<Subscription> hasDate(Date startDate){
		if(startDate!=null) {
			return (filter,cq,cb)->cb.lessThanOrEqualTo(filter.get("createdDate"), startDate);
		}
		return null;
	}
}