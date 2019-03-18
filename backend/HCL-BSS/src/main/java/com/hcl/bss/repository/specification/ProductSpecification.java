package com.hcl.bss.repository.specification;
import java.util.Date;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.hcl.bss.domain.Product;

//public class ProductSpecification implements Specification<Product> {
public class ProductSpecification {
	
	private  Product filter;
	
	public ProductSpecification(Product filter) {
		super();
		this.filter = filter;
	}


// cq --> CriteriaQuery , cb--> CriteriaBuilder
	public static Specification<Product> hasProductName(String productName){
		if(productName != null) {
		return (filter, cq,cb)-> cb.like(filter.get("productDispName"), "%" + productName + "%");
		}
		return null;
	}
	public static Specification<Product> hasSku(String sku){
		if (sku != null) {
			return (filter, cq,cb)-> cb.like(filter.get("sku"), "%" + sku + "%");
		}
		return null;
	}
	
	
	public static Specification<Product> isActive(Integer isActive){
		if(isActive != null) {
		return (filter, cq,cb)-> cb.equal(filter.get("isActive"), isActive);
		}
		return null;
	}
	
	public static Specification<Product> hasStartDate(Date startDate, Date endDate){
		if(startDate != null) {
		
			return (filter, cq,cb)-> {
				Predicate startDatePredicate = cb.greaterThanOrEqualTo(filter.get("productStartDate").as(java.sql.Date.class),startDate);
				Predicate endDatePredicate = cb.lessThanOrEqualTo(filter.get("productExpDate").as(java.sql.Date.class),endDate);
				Predicate and = cb.and(startDatePredicate, endDatePredicate);
				return and;
			};
	}
		return null;
	}
	public static Specification<Product> hasCode(String code){
		if (code != null) {
			return (filter, cq,cb)-> cb.like(filter.get("productTypeCode").get("productTypeCode"), "%" + code + "%");
		}
		return null;
	}
	
	
}