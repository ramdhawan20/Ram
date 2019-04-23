package com.hcl.bss.repository;

import com.hcl.bss.domain.Role;
import com.hcl.bss.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * @author- Aditya gupta
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role>, PagingAndSortingRepository<Role, Long> {
    Role findByRoleName(String role);
}