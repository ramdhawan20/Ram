package com.hcl.bss.services;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductPlanAssociationDto;

/**
 * Interface for Product services 
 */

public interface ProductService {

	Product addProduct(ProductDto product);

	ProductDataDto  getAllProducts(Pageable reqCount);

	Iterable<ProductTypeMaster> getProductType();

	ProductDataDto searchProducts(ProductDto product, Pageable reqCount, HttpServletResponse response);

	String associatePlan(ProductPlanAssociationDto productPlan);

}
