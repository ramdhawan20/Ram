package com.hcl.bss.validator;

import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DDMMYYYY;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DD_MM_YYYY;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.bss.dto.ProductDto;

public class CustomDateSchemeValidator implements ConstraintValidator<CustomDateScheme, Object> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomDateSchemeValidator.class);

	private String field;
	ProductDto dto = new ProductDto();

	public void initialize(CustomDateScheme constraintAnnotation) {
		this.field = constraintAnnotation.field();

	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean secondCHeck = false;

		dto = (ProductDto) value;

		System.out.println("VAL:" + dto.getProductExpDate());

		System.out.println("VAL3:" + value);

		if (dto.getProductExpDate().trim().equals("")) {
			return false;
		} else {
			boolean firstCheck = formatDDMMYYYY();
			if (!firstCheck) {
				boolean secondCheck = formatYYYYMMDD();
				if (!secondCheck) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}

		}

	}

	private boolean formatYYYYMMDD() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);
		formatter.setLenient(false);
		try {

			Date strDate = formatter.parse(dto.getProductExpDate().trim());
		} catch (ParseException de) {
			LOGGER.error("Error", de);
			// de.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean formatDDMMYYYY() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY);
		formatter.setLenient(false);
		try {

			Date strDate = formatter.parse(dto.getProductExpDate().trim());
		} catch (ParseException de) {
			de.printStackTrace();
			return false;
		}
		return true;
	}

}
