package com.hcl.bss.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.DropDownOutDto;
import com.hcl.bss.dto.ErrorResponseDTO;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductPlanAssociationDto;
import com.hcl.bss.dto.ResponseDto;
import com.hcl.bss.dto.StatusDto;
import com.hcl.bss.services.ProductService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@PropertySource("classpath:application.properties")
public class ProductController {
	@Autowired
	ProductService productService;
	@Value("${app.page.size}")
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
		
		Pageable reqCount = PageRequest.of(pageNumber, recordPerPage);
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
	public ResponseEntity<ProductDataDto> searchProducts(@RequestBody ProductDto product,HttpServletResponse response) {
		ProductDataDto productData = new  ProductDataDto();
		Integer pageNumber = Integer.valueOf(product.getPageNo());
		
		Pageable reqCount = PageRequest.of(pageNumber, recordPerPage);
		try {
			productData = productService.searchProducts(product,reqCount,response);
			return new ResponseEntity<ProductDataDto>(productData, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ProductDataDto>(productData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	@ApiOperation(value = "Associate Product with Plan", response = String.class)
	@RequestMapping(value = "/associatePlan",produces = { "application/json" },method = RequestMethod.POST)
	public ResponseEntity<StatusDto> accociatePlan(@RequestBody ProductPlanAssociationDto productPlan) {
		StatusDto status = new StatusDto();
		 String msg = productService.associatePlan(productPlan);
		 status.setMsg(msg);
		 return new ResponseEntity<StatusDto>(status, HttpStatus.OK);
	}


	@ApiOperation(value = "Get Dropdown Data", response = String.class)
	@RequestMapping(value = "/getProductDropDown",method = RequestMethod.POST)
	public ResponseEntity<DropDownOutDto> dropDownData(@RequestParam String statusId) {
		DropDownOutDto dropDownOutDto = new DropDownOutDto();
		try {
			List<String> dropDownList = productService.getDropDownData(statusId);
			if(dropDownList!=null && !dropDownList.isEmpty()) {
				dropDownOutDto.setMessage("Drop Down Fetched Successfully");
				dropDownOutDto.setResponseCode(HttpStatus.OK.value());
				dropDownOutDto.setSuccess(true);
				dropDownOutDto.setDropDownList(dropDownList);
				return new ResponseEntity<DropDownOutDto>(dropDownOutDto,HttpStatus.OK);
			}		
			else {
				dropDownOutDto.setMessage("Drop Down values not found in Database");
				dropDownOutDto.setResponseCode(HttpStatus.NOT_FOUND.value());
				dropDownOutDto.setSuccess(false);
				return new ResponseEntity<DropDownOutDto>(dropDownOutDto,HttpStatus.NOT_FOUND);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			dropDownOutDto.setMessage(e.getMessage());
			dropDownOutDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			dropDownOutDto.setSuccess(false);
			return new ResponseEntity<DropDownOutDto>(dropDownOutDto,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value="Update Product",response = ResponseDto.class)
	@RequestMapping(value="/updateProduct", produces= {"application/json"},method=RequestMethod.POST)
	public ResponseEntity<ResponseDto> updateProduct(@RequestBody ProductDto product){
		ResponseDto responseDto = new ResponseDto();
		try{
			responseDto = productService.updateProduct(product);
			return new ResponseEntity<>(responseDto,HttpStatus.OK);
		}
		catch (Exception e) {
			
			responseDto.setResponseCode(404);
			responseDto.setResponseStatus("Fail");
			responseDto.setMessage("Product could not be update");
			return new ResponseEntity<>(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
 		}
	}
}
