package com.hcl.bss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.bss.domain.RoleMenuMapping;
/**
 *
 * @author- Vinay Panwar
 */
public interface RoleMenuMappingRepository extends JpaRepository<RoleMenuMapping, Long> {
	
	List<RoleMenuMapping> findByRoleUid(Long roleUid);
	
}
