package com.hcl.bss.services;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;

/**
 * Interface for Product services 
 */

public interface ProductService {

	Product addProduct(ProductDto product);

	ProductDataDto  getAllProducts(Pageable reqCount);

	Iterable<ProductTypeMaster> getProductType();

	ProductDataDto searchProducts(ProductDto product, Pageable reqCount);

}
