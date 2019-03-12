package com.hcl.bss.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.domain.Subscription1;
import com.hcl.bss.dto.ErrorResponseDTO;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.services.ProductService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@PropertySource("classpath:application.properties")
public class ProductController {
	@Autowired
	ProductService productService;
	@Value("${default.recordPerPage:10}")
	 Integer recordPerPage;

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
	@RequestMapping(value = "/getProducts/{pageNo}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<ProductDataDto> getAllProduct(@PathVariable("pageNo") String pageNo) {
		Integer pageNumber = Integer.valueOf(pageNo);
		@SuppressWarnings("deprecation")
		Pageable reqCount = new PageRequest(pageNumber, recordPerPage);
		ProductDataDto productData = new  ProductDataDto();
		productData = productService.getAllProducts(reqCount);
		return new ResponseEntity<>(productData, HttpStatus.OK);

	}

	@ApiOperation(value = "Get Product Type", response = ProductDto.class)
	@RequestMapping(value = "/getProductType", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<ProductTypeMaster>> getProductType() {
		Iterable<ProductTypeMaster> productList = new ArrayList<>();
		productList = productService.getProductType();
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}
	@ApiOperation(value = "Get list of Product Based on Search Criteria", response = Product.class)
	@PostMapping(value = "/searchProducts")
	public ResponseEntity<ProductDataDto> searchProducts(@RequestBody ProductDto product) {
		ProductDataDto productData = new  ProductDataDto();
		Integer pageNumber = Integer.valueOf(product.getPageNo());
		@SuppressWarnings("deprecation")
		Pageable reqCount = new PageRequest(pageNumber, recordPerPage);
		try {
			productData = productService.searchProducts(product,reqCount);

			if(productData.getProductList() != null && productData.getProductList().size() > 0) {
				return new ResponseEntity<ProductDataDto>(productData, HttpStatus.OK);
			}else {
				
				return new ResponseEntity<ProductDataDto>(productData, HttpStatus.NOT_FOUND);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ProductDataDto>(productData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		
}
