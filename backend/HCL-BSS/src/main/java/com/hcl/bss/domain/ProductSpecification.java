package com.hcl.bss.domain;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.hcl.bss.domain.Product;

public class ProductSpecification implements Specification<Product> {
	
	private Product filter;
	
	public ProductSpecification(Product filter) {
		super();
		this.filter = filter;
	}

	public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> cq,
			CriteriaBuilder cb) {
		
		Predicate p = cb.disjunction();

		/*if (filter.getProductDispName() != null) {
			p.getExpressions().add(cb.equal(root.get("productDispName"), filter.getProductDispName()));
		}
		if (filter.getSku() != null) {
			p.getExpressions().add(cb.equal(root.get("sku"), filter.getSku()));
		}
		if (filter.getProductStartDate() != null) {
			p.getExpressions().add(cb.equal(root.get("productStartDate"), filter.getProductStartDate()));
		}
		if (filter.getProductExpDate() != null) {
			p.getExpressions().add(cb.equal(root.get("productExpDate"), filter.getProductExpDate()));
		}
		p.getExpressions().add(cb.equal(root.get("isActive"), filter.getIsActive()));*/
		 if (filter.getProductDispName() != null && filter.getSku() != null) {
	            p.getExpressions().add(
	                    cb.and(cb.equal(root.get("productDispName"), filter.getProductDispName()),
	                            cb.equal(root.get("sku"), filter.getSku())));
	        }
		
		return p;
		
	}

	
}