package com.hcl.bss.repository;

import com.hcl.bss.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}
