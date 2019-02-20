package com.hcl.bss.fieldValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductErrorLogDetails;
import com.hcl.bss.dto.ProductUploadDetails;
import com.hcl.bss.util.ValidationUtil;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_MMDDYYYY;

@Component
public class DataMigrationFieldValidator {
	@Autowired
	ValidationUtil validationUtil;

	public ProductUploadDetails validateFields(List<ProductDto> listProduct) {
		
		List<ProductErrorLogDetails> errorList = new ArrayList<>();
		ProductErrorLogDetails errorDetails = null;
		ProductUploadDetails productUploadDetails = new ProductUploadDetails();
		List<ProductDto> successProductList = new ArrayList<>();
		int rowNumber = 1;
		
		for(ProductDto element : listProduct) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<ProductDto>> violations = validator.validate(element);
			if(violations.size() == 0) {
				successProductList.add(element);
				continue;
			}
			for (ConstraintViolation<ProductDto> violation : violations) {
				errorDetails = new ProductErrorLogDetails();
				//Below Error Details to be preserved for file export
			    System.out.println("Row Number:" + rowNumber + " Error: " + violation.getMessage()); 
			    errorDetails.setRowNo(rowNumber);
			    errorDetails.setErrorMsg(violation.getMessage());
			    errorList.add(errorDetails);		    
			}
			
			rowNumber++;
			
		}
		
		productUploadDetails.setErrorLogDetailsList(errorList);
		productUploadDetails.setValidProductList(successProductList);
		return productUploadDetails;	

}

	private boolean isValidDate(String productExpDate) {
		//TODO - Put this logic in CustomDateValidator
		 SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_MMDDYYYY);
		 try {
			 Date strDate= formatter.parse(productExpDate);
		 }catch(ParseException de) {
			 de.printStackTrace();
		 }
		return false;
		
	}

}
