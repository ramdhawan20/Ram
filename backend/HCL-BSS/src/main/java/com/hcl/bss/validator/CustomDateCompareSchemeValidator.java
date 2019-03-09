package com.hcl.bss.validator;

import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DDMMYYYY;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.DEFAULT_EXP_DATE;
import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.HYPHEN;

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
		String strDate = BLANK;
		dto = (ProductDto) value;
		boolean isStartDateBeforeExpDate = false;
System.out.println(dto.getProductStartDate());
		if(BLANK.equalsIgnoreCase(dto.getProductStartDate())) {
			Date startDate = new Date();
			 DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);  
            strDate = dateFormat.format(startDate);  
			dto.setProductStartDate(strDate);
		}
		 if(BLANK.equalsIgnoreCase(dto.getProductExpDate())) {
			 dto.setProductExpDate(DEFAULT_EXP_DATE);
		 }
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);
		SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);
		if(dto.getProductStartDate() == null) {
		
		}
		
		try {
			isStartDateBeforeExpDate = sdf.parse(dto.getProductStartDate()).before(sdf.parse(dto.getProductExpDate()));
		} catch (ParseException e) {
			try {
			isStartDateBeforeExpDate = sdf1.parse(dto.getProductStartDate()).before(sdf1.parse(dto.getProductExpDate()));
			}catch(ParseException pe) {
				try {
					isStartDateBeforeExpDate = sdf.parse(dto.getProductStartDate()).before(sdf1.parse(dto.getProductExpDate()));
				}catch(ParseException pex){
					try {
					isStartDateBeforeExpDate = sdf1.parse(dto.getProductStartDate()).before(sdf.parse(dto.getProductExpDate()));
					}catch(ParseException ex) {
						
					}
				}
			}
		}
		
		return isStartDateBeforeExpDate;
}
}