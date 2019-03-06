package com.hcl.bss.validator;

import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DDMMYYYY;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.DEFAULT_EXP_DATE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.bss.dto.ProductDto;

public class CustomDateCompareSchemeValidator implements ConstraintValidator<CustomDateCompareScheme, Object> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomDateCompareSchemeValidator.class);

	private String expDate;
	private String startDate;
	ProductDto dto = new ProductDto();

	public void initialize(CustomDateCompareScheme constraintAnnotation) {
		this.expDate = constraintAnnotation.expDate();
		this.startDate = constraintAnnotation.startDate();

	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		dto = (ProductDto) value;
		boolean isStartDateBeforeExpDate = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(dto.getProductStartDate() == null) {
			Date startDate = new Date();
			 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
             String strDate = dateFormat.format(startDate);  
			dto.setProductStartDate(strDate);
		}
		if(dto.getProductExpDate() == null) { 
			dto.setProductStartDate(DEFAULT_EXP_DATE);
		}
		try {
			isStartDateBeforeExpDate = sdf.parse(dto.getProductStartDate()).before(sdf.parse(dto.getProductExpDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return isStartDateBeforeExpDate;

}
}