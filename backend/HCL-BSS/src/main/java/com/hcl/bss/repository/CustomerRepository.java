package com.hcl.bss.repository;

import com.hcl.bss.domain.Customer;
import com.hcl.bss.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author- Aditya gupta
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findBySubscriptions(Subscription subscription);
}
