package com.hcl.bss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hcl.bss.domain.Role;
/**
 *
 * @author- Vinay Panwar
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	@Query("SELECT DISTINCT role.roleName FROM Role role")
	List<String> getAllRoleName();
	 
	Role findByRoleName(String roleName);

}
