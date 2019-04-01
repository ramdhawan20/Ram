package com.hcl.bss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.hcl.bss.domain.Currency;
@Repository
public interface CurrencyMasterRepository extends JpaRepository<Currency, String> {
	
	@Query(value= "SELECT DISTINCT CURRENCY_CODE from tb_currency_master",nativeQuery = true)
	List<String> getCurrency();
	 
	 
}
