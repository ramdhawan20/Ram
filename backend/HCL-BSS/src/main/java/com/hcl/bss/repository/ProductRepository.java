package com.hcl.bss.repository;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hcl.bss.dao.ProductDAO;
import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.domain.Product;
import com.hcl.bss.dto.ProductDto;
@Repository
@Transactional
public class ProductRepository implements ProductDAO {
	
	
	 private SessionFactory sessionFactory;
	    @Autowired
	    public ProductRepository(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	@Override
	public List<ProductDto> saveProduct(List<ProductDto> listProduct) throws ParseException {
		
		 Session session = this.sessionFactory.getCurrentSession();
		
		
		for(ProductDto prod :listProduct) {
			Product product = new Product();
			product.setProductId(prod.getProductId());
			product.setSku(prod.getSku());
			product.setProductDispName(prod.getProductDispName());
			product.setCreatedDate(new Date());
			String expDate = prod.getProductExpDate();
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(expDate);  
			product.setProductExpDate(date1);
			session.save(product);
			System.out.println(product.toString());
			
		}
		
		return listProduct;
	}
	
	

}
