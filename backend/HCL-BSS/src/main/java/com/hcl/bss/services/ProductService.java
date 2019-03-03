package com.hcl.bss.services;

import java.util.List;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.ProductDto;

/**
 * Interface for Product services 
 */

public interface ProductService {

	Product addProduct(ProductDto product);

	List<ProductDto>  getAllProducts();

	Iterable<ProductTypeMaster> getProductType();

}
