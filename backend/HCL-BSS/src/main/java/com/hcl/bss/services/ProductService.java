package com.hcl.bss.services;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.ProductDto;

/**
 * Interface for Product services 
 */

public interface ProductService {

	Product addProduct(ProductDto product);

	Iterable<Product> getAllProducts();

	Iterable<ProductTypeMaster> getProductType();

}
