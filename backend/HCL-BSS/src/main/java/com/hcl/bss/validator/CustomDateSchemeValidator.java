package com.hcl.bss.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_MMDDYYYY;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import com.hcl.bss.dto.ProductDto;

public class CustomDateSchemeValidator 
implements ConstraintValidator<CustomDateScheme, Object> {

  private String field;
  

  public void initialize(CustomDateScheme constraintAnnotation) {
      this.field = constraintAnnotation.field();
      
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {


      ProductDto dto = (ProductDto)value;
      
      System.out.println("VAL:" + dto.getProductExpDate());
      
      System.out.println("VAL3:" + value);
      
      
      
      SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_MMDDYYYY);
      formatter.setLenient(false);
 	 try {
 		
 		 Date strDate= formatter.parse(dto.getProductExpDate().trim());
 	 }catch(ParseException de) {
 		 de.printStackTrace();
 		 return false;
 	 }
 	return true;
		
	}


} 
  


