package com.hcl.bss.validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductErrorLogDetails;
import com.hcl.bss.dto.ProductUploadDetails;
import com.hcl.bss.repository.ProductRepository;
import static com.hcl.bss.constants.ApplicationConstants.DUPLICATE_SKU;
import static com.hcl.bss.constants.ApplicationConstants.ROW_NO;
import static com.hcl.bss.constants.ApplicationConstants.ERROR;
import static com.hcl.bss.constants.ApplicationConstants.DUPLICATE_SKU_DB;
import static com.hcl.bss.constants.ApplicationConstants.DEFAULT_EXP_DATE;
import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.PRODUCT_TYPE_CODE_NOT_DEFIENED_IN_MASTER;

@Component
public class DataMigrationFieldValidator {
	@Autowired
	ProductRepository productRepository;

	public ProductUploadDetails validateFields(List<ProductDto> listProduct, Set<String> skuSetDB, Set<String> productTypeCodeDB) {
		
		List<ProductErrorLogDetails> errorList = new ArrayList<>();
		ProductErrorLogDetails errorDetails = null;
		ProductUploadDetails productUploadDetails = new ProductUploadDetails();
		List<ProductDto> successProductList = new ArrayList<>();
		Set<String> skuSet = new HashSet<>();
		int rowNumber = 1;
		for (ProductDto element : listProduct) {
			rowNumber++;
			boolean duplicateSkuCsv = false;
			boolean duplicateSkuDB = false;
			boolean productTypeCodeFKConstraintViolation = false;
			if(!productTypeCodeDB.contains(element.getProductTypeCode())) {
				errorDetails = new ProductErrorLogDetails();
				errorDetails.setRowNo(rowNumber);
				errorDetails.setErrorMsg(PRODUCT_TYPE_CODE_NOT_DEFIENED_IN_MASTER +element.getProductTypeCode());
				errorList.add(errorDetails);
				productTypeCodeFKConstraintViolation = true;
			}
			if(element.getProductExpDate() == null || BLANK.equals(element.getProductExpDate())) {
				element.setProductExpDate(DEFAULT_EXP_DATE);
			}
			// Check Duplicate In CSV
			if(skuSet.contains(element.getSku())) {
				errorDetails = new ProductErrorLogDetails();
			errorDetails.setRowNo(rowNumber);
			errorDetails.setErrorMsg(DUPLICATE_SKU +element.getSku());
			errorList.add(errorDetails);
			duplicateSkuCsv = true; 
			}
			skuSet.add(element.getSku());
			// Check if SKU already exist in DB
			if(skuSetDB.contains(element.getSku())) {
				errorDetails = new ProductErrorLogDetails();
				errorDetails.setRowNo(rowNumber);
				errorDetails.setErrorMsg(DUPLICATE_SKU_DB +element.getSku());
				errorList.add(errorDetails);
				duplicateSkuDB = true; 
				}
				skuSet.add(element.getSku());
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<ProductDto>> violations = validator.validate(element);
			if((!duplicateSkuCsv && !duplicateSkuDB) && !productTypeCodeFKConstraintViolation) {
			if (violations.size() == 0) {
				successProductList.add(element);
				continue;
			}
			}
			for (ConstraintViolation<ProductDto> violation : violations) {
				
				// Below Error Details to be preserved for file export
				System.out.println(ROW_NO + rowNumber + ERROR + violation.getMessage());
				errorDetails = new ProductErrorLogDetails();
				errorDetails.setRowNo(rowNumber);
				errorDetails.setErrorMsg(violation.getMessage());
				errorList.add(errorDetails);
			}

		}

		productUploadDetails.setErrorLogDetailsList(errorList);
		productUploadDetails.setValidProductList(successProductList);
		return productUploadDetails;

	}

}
