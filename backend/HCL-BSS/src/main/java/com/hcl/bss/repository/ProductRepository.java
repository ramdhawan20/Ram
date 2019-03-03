package com.hcl.bss.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hcl.bss.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	 @Query("SELECT p.sku  FROM Product p")
	Set<String> getSkus();

}
