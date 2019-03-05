package com.hcl.bss.repository;

import com.hcl.bss.domain.ProductTypeMaster;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 *
 * @author- Aditya gupta
 */
public interface ProductTypeMasterRepository extends JpaRepository<ProductTypeMaster, String> {
	
	 @Query("SELECT ptm.productTypeCode  FROM ProductTypeMaster ptm")
	Set<String> getProductTypeCode();
}
