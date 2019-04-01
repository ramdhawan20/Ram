package com.hcl.bss.repository;

import com.hcl.bss.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
/**
 *
 * @author- Aditya gupta
 */
public interface OrderRepository extends JpaRepository<Order, Long>,JpaSpecificationExecutor<Order> {

    public List<Order> findAll();
}
