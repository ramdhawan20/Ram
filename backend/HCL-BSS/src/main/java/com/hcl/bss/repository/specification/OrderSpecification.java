package com.hcl.bss.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.hcl.bss.domain.Order;

public class OrderSpecification {

	private Order filter;

	public OrderSpecification(Order filter) {
		super();
		this.filter = filter;
	}
	
	public static Specification<Order> hasProdutId(Long productId){
		if(productId!=null) {
			return (filter,cq,cb)->cb.equal(filter.get("productId"), productId);
		}
		return null;
	}
 }
