package com.hcl.bss.repository.specification;

import java.util.Date;

import com.hcl.bss.domain.BatchLog;
import org.springframework.data.jpa.domain.Specification;

public class BatchLogSpecification {
	
	private BatchLog filter;
	public BatchLogSpecification(BatchLog filter) {
		super();
		this.filter=filter;
	}
	public static Specification<BatchLog> hasStartDate(Date startDate){
		if(startDate!=null) {
			return (filter,cq,cb)->cb.greaterThanOrEqualTo(filter.get("runDownDate"), startDate);
		}
		return null;
	}
	
	public static Specification<BatchLog> hasEndDate(Date endDate){
		if(endDate!=null) {
			return (filter,cq,cb)->cb.lessThanOrEqualTo(filter.get("runDownDate"), endDate);
		}
		return null;
	}
	public static Specification<BatchLog> hasStatus(String status){
		if(status!=null&&(!status.equalsIgnoreCase(""))) {
			return (filter,cq,cb)->cb.equal(filter.get("status"), status);
		}
		return null;
	}
}
