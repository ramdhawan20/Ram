package com.hcl.bss.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductErrorLogDetails;
import com.hcl.bss.dto.ProductUploadDetails;

@Component
public class DataMigrationFieldValidator {

	public ProductUploadDetails validateFields(List<ProductDto> listProduct) {

		List<ProductErrorLogDetails> errorList = new ArrayList<>();
		ProductErrorLogDetails errorDetails = null;
		ProductUploadDetails productUploadDetails = new ProductUploadDetails();
		List<ProductDto> successProductList = new ArrayList<>();
		int rowNumber = 1;

		for (ProductDto element : listProduct) {
			rowNumber++;
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<ProductDto>> violations = validator.validate(element);
			if (violations.size() == 0) {
				successProductList.add(element);
				continue;
			}
			for (ConstraintViolation<ProductDto> violation : violations) {
				errorDetails = new ProductErrorLogDetails();
				// Below Error Details to be preserved for file export
				System.out.println("Row Number:" + rowNumber + " Error: " + violation.getMessage());
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
