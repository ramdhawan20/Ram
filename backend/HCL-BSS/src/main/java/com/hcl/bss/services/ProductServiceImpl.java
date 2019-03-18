package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.domain.RatePlan;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductPlanAssociationDto;
import com.hcl.bss.dto.RatePlanProductDto;
import com.hcl.bss.repository.ProductRepository;
import com.hcl.bss.repository.ProductTypeMasterRepository;
import com.hcl.bss.repository.RatePlanRepository;
import com.hcl.bss.repository.specification.ProductSpecification;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProductTypeMasterRepository productTypeMasterRepository;
	@Autowired
	RatePlanRepository ratePlanRepository;
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
		if(null != expDate) {
		try {
			eDate = new SimpleDateFormat(DD_MM_YYYY).parse(expDate);
		} catch (Exception e) {
		}
		
		productEntity.setProductExpDate(eDate);
		try {
			eDate = new SimpleDateFormat(DD_MM_YYYY).parse(expDate);
		} catch (Exception e) {
		}
		}
		if(null != startDate) {
			try {
				sDate = new SimpleDateFormat(DD_MM_YYYY).parse(startDate);
			} catch (Exception e) {
			}
			productEntity.setProductStartDate(sDate);
			try {
				eDate = new SimpleDateFormat(DD_MM_YYYY).parse(startDate);
			} catch (Exception e) {
			}
			productEntity.setProductStartDate(sDate);
			}
		

		return productEntity;

	}

	@Override
	public ProductDataDto getAllProducts(Pageable reqCount) {
		ProductDataDto productData = new ProductDataDto();
		Iterable<Product> productEntityList = new ArrayList<>();
		List<ProductDto> productDtoList = new ArrayList<>();
		Long noOfTotalRecords = 0L;
		noOfTotalRecords = productRepository.count();
		productEntityList =  productRepository.findAll(reqCount);
		Integer pageNumber = reqCount.getPageNumber()+1;
		Integer lastRecord = pageNumber * reqCount.getPageSize();
		
		if(noOfTotalRecords - lastRecord <= 0) {
			if(noOfTotalRecords/lastRecord ==0) {
				productData.setLastPage(true);
			System.out.println("Last page to show");
		}
		}
		productDtoList = convertProductEntityToDto(productEntityList);
		productData.setProductList(productDtoList);
		return productData;
	}
	private List<ProductDto> convertProductEntityToDto(Iterable<Product> productEntityList) {
		List<ProductDto> ProductDtoList = new ArrayList<>();
		
		Set<RatePlan> ratePlanSet = new HashSet<RatePlan>();
		
		for(Product product :productEntityList) {
			Set<RatePlanProductDto> ratePlans = new HashSet<RatePlanProductDto>();
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
			prod.setUidpk(product.getUidpk());
			prod.setProductDispName(product.getProductDispName());
			prod.setProductTypeCode(product.getProductTypeCode().getProductTypeCode());
			prod.setProductDescription(product.getProductDescription());
			prod.setSku(product.getSku());
			prod.setProductStartDate(sDate);
			prod.setProductExpDate(eDate);
			ratePlanSet = product.getRatePlans();
			for(RatePlan ratePlan : ratePlanSet) {
				RatePlanProductDto rpDto = new RatePlanProductDto();
				
				if(ratePlan.getIsActive() == 0) {
				rpDto.setIsActive("InActive");
				}
				else {
					rpDto.setIsActive("Active");
				}
				rpDto.setUidpk(ratePlan.getId());
				rpDto.setRatePlanId(ratePlan.getRatePlanId());
				rpDto.setName(ratePlan.getRatePlanDescription());
				rpDto.setBillEvery(ratePlan.getBillingFrequency());
				rpDto.setPrice(ratePlan.getPrice());
				if(null != ratePlan.getUom()) {
				rpDto.setUnitOfMesureId(ratePlan.getUom().getUnitOfMeasure());
				}
				ratePlans.add(rpDto);
			}
			
			prod.setRatePlans(ratePlans);
			if(!prod.getRatePlans().isEmpty()) {
				prod.setAssociatedWithPlan(true);
			}
			ProductDtoList.add(prod);
			
		}
		return ProductDtoList;
	}
	@Override
	public Iterable<ProductTypeMaster> getProductType() {

		return productTypeMasterRepository.findAll();
	}

	@Override
	public ProductDataDto searchProducts(ProductDto product, Pageable reqCount) {
		List<ProductDto> productDtoList = new ArrayList<>();
		ProductDataDto productDataDto = new ProductDataDto();
		List<Product> filteredData = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;
		Integer activeInactive = null;
		String productDispName = product.getProductDispName();
		String code = product.getProductTypeCode();
		String sku = product.getSku();
		String status = product.getStatus();
		if("Active".equalsIgnoreCase(status)) {
		activeInactive = 1;
		}
		else {
			activeInactive = 0;
		}
		String sDate = product.getProductStartDate();
		String eDate = product.getProductExpDate();
		if(null != sDate) {
		try {
			startDate = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		}
		if(null != eDate) {
		try {
			endDate = new SimpleDateFormat("dd/MM/yyyy").parse(eDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		}

		 Product filter = new Product();
		 filter.setProductDispName(productDispName);
		 filter.setIsActive(activeInactive);
		 filter.setSku(sku);
		 filter.setProductExpDate(endDate);
		 filter.setProductStartDate(startDate);
		 ProductTypeMaster ptm = new ProductTypeMaster();
		 ptm.setProductTypeCode(code);
		 filter.setProductTypeCode(ptm);

		 Page<Product> result = productRepository.findAll(Specification.where(ProductSpecification.hasProductName(productDispName)).and(ProductSpecification.hasSku(sku)).and(ProductSpecification.isActive(activeInactive)).and(ProductSpecification.hasStartDate(startDate,endDate)).and(ProductSpecification.hasCode(code)),reqCount);
		 filteredData = result.getContent();
		 productDtoList = convertProductEntityToDto(filteredData);
		 productDataDto.setProductList(productDtoList);
		 Integer pageNumber = reqCount.getPageNumber()+1;
			Integer lastRecord = pageNumber * reqCount.getPageSize();
			
			if(filteredData.size() - lastRecord <= 0) {
				if(filteredData.size()/lastRecord ==0) {
					productDataDto.setLastPage(true);
				System.out.println("Last page to show");
			}
			}
		return productDataDto;
		
	}

	@Override
	public String associatePlan(ProductPlanAssociationDto productPlan) {
		String msg = "Failed" ;
		RatePlan ratePlan = new RatePlan();
		Long productId = productPlan.getProduct().getUidpk();
		Product prod = new Product();
		prod = productRepository.getOne(productId);
		Set<RatePlan> ratePlanSet = new HashSet<RatePlan>();
		ratePlanSet = prod.getRatePlans();
		Long rpId = productPlan.getRatePlan().getUidpk();
		ratePlan = ratePlanRepository.getOne(rpId);
		ratePlanSet.add(ratePlan);
		prod.setRatePlans(ratePlanSet);
		
			 try {
		productRepository.save(prod);
		msg= "Successfully Associated product with Plan";
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		return msg;
	}

}
