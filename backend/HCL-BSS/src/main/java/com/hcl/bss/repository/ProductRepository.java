package com.hcl.bss.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hcl.bss.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}
