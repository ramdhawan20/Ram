package com.hcl.bss.repository;

import com.hcl.bss.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
}
