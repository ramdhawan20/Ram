package com.hcl.bss.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.ErrorResponseDTO;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.services.ProductService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
public class ProductController {
	@Autowired
	ProductService productService;

	@ApiOperation(value = "Add product", response = ProductDto.class)
	@RequestMapping(value = "/product", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@RequestBody ProductDto product) {
		Product prod = productService.addProduct(product);
		return new ResponseEntity<>(prod, HttpStatus.OK);

	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDTO> sqlexceptionHandler(DataIntegrityViolationException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
             if(ex.getMessage().contains("constraint")) {
                 if (ex.getMessage().contains("SKU_UNIQUE")) {
                	 error.setErrorCode(1062);
                	 error.setMessage("Duplicate SKU");
                 } 
                 else {
                	 error.setErrorCode(0);
                	 error.setMessage("Database Issue");
                 }
             }
		return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.CONFLICT);
	}

	@ApiOperation(value = "Get All Product", response = ProductDto.class)
	@RequestMapping(value = "/getProducts", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<List<ProductDto>> getAllProduct() {
		List<ProductDto> productList = new ArrayList<>();
		productList = productService.getAllProducts();
		return new ResponseEntity<>(productList, HttpStatus.OK);

	}

	@ApiOperation(value = "Get Product Type", response = ProductDto.class)
	@RequestMapping(value = "/getProductType", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<ProductTypeMaster>> getProductType() {
		Iterable<ProductTypeMaster> productList = new ArrayList<>();
		productList = productService.getProductType();
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}
}
