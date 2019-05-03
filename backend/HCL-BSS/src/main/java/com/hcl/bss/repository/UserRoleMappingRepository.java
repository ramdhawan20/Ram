package com.hcl.bss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.bss.domain.UserRoleMapping;
/**
 *
 * @author- Vinay Panwar
 */
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
	
	List<UserRoleMapping> findByRoleUid(Long roleUid);
	
}
