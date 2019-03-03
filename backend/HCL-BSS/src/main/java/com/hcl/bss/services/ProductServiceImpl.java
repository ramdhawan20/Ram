package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<ProductDto> getAllProducts() {
		Iterable<Product> productEntityList = new ArrayList<>();
		List<ProductDto> productDtoList = new ArrayList<>();

		productEntityList =  productRepository.findAll();
		productDtoList = convertProductEntityToDto(productEntityList);
		return productDtoList;
	}
	private List<ProductDto> convertProductEntityToDto(Iterable<Product> productEntityList) {
		List<ProductDto> ProductDtoList = new ArrayList<>();
		for(Product product :productEntityList) {
			ProductDto prod = new ProductDto();
			String eDate = BLANK;
			String sDate = BLANK;
			Date startDate = product.getProductStartDate();
			Date expDate = product.getProductExpDate();
			DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
			if(expDate != null) {
			eDate = dateFormat.format(expDate); 
			}
			if(startDate != null) {
			sDate = dateFormat.format(startDate);
			}
			prod.setProductDispName(product.getProductDispName());
			prod.setProductTypeCode(product.getProductTypeCode().getProductTypeCode());
			prod.setProductDescription(product.getProductDescription());
			prod.setSku(product.getSku());
			prod.setProductStartDate(sDate);
			prod.setProductExpDate(eDate);
			ProductDtoList.add(prod);
			
		}
		return ProductDtoList;
	}
	@Override
	public Iterable<ProductTypeMaster> getProductType() {

		return productTypeMasterRepository.findAll();
	}

}
