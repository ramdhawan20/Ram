package com.hcl.bss.repository;

import com.hcl.bss.domain.Customer;
import com.hcl.bss.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * @author- Aditya gupta
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findBySubscriptions(Subscription subscription);

    @Query("select DISTINCT c from Customer c join c.subscriptions s where s.isActive=:isActive and s.autorenew=:autorenew")
    public List<Customer> findBySubscriptions(@Param("isActive")Integer isActive, @Param("autorenew")Integer autorenew);
}
