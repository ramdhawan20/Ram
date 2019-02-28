package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.repository.ProductRepository;
import com.hcl.bss.repository.ProductTypeMasterRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProductTypeMasterRepository productTypeMasterRepository;

	@Override
	public Product addProduct(ProductDto product) {

		Product productEntity = null;
		productEntity = convertProductDTOListToEntityList(product);
		try {
		productEntity = productRepository.save(productEntity);
		}catch(DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Error: ",ex);
		}
		return productEntity;
	}

	private Product convertProductDTOListToEntityList(ProductDto product) {
		Date sDate = null;
		Date eDate = null;
		ProductTypeMaster ptm = new ProductTypeMaster();
		ptm.setProductTypeCode(product.getProductTypeCode());
		Product productEntity = new Product();
		productEntity.setProductTypeCode(ptm);
		productEntity.setProductDispName(product.getProductDispName());
		productEntity.setProductDescription(product.getProductDescription());
		productEntity.setSku(product.getSku());
		String startDate = product.getProductStartDate();
		String expDate = product.getProductExpDate();
		try {
			sDate = new SimpleDateFormat(DD_MM_YYYY).parse(expDate);
		} catch (Exception e) {
		}
		productEntity.setProductStartDate(sDate);
		try {
			eDate = new SimpleDateFormat(DD_MM_YYYY).parse(expDate);
		} catch (Exception e) {
		}
		productEntity.setProductExpDate(eDate);

		return productEntity;

	}

	@Override
	public Iterable<Product> getAllProducts() {

		return productRepository.findAll();
	}

	@Override
	public Iterable<ProductTypeMaster> getProductType() {

		return productTypeMasterRepository.findAll();
	}

}
