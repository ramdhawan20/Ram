package com.hcl.bss.repository;

import com.hcl.bss.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
