package com.hcl.bss.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.bss.domain.Product;
import com.hcl.bss.dto.ProductDto;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>,JpaSpecificationExecutor<Product> {
	 @Query("SELECT p.sku  FROM Product p")
	Set<String> getSkus();

	 
	 
	 
	 
	 

}
