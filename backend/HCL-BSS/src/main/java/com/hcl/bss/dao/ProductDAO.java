package com.hcl.bss.dao;

import java.io.FileWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.dto.ErrorCsvFileDto;
import com.hcl.bss.dto.ProductDto;

public interface ProductDAO {

	List<ProductDto> saveProduct(List<ProductDto> listProduct) throws ParseException;

	Serializable saveErrorLog(ErrorCsvFile errorCsvFile);

	ErrorCsvFile findById(Long fileId);

}
