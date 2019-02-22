package com.hcl.bss.dao;

import java.text.ParseException;
import java.util.List;

import com.hcl.bss.dto.ProductDto;

public interface ProductDAO {

	List<ProductDto> saveProduct(List<ProductDto> listProduct) throws ParseException;

	

}
