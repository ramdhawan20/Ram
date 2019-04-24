package com.hcl.bss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.bss.domain.SubMenu;
/**
 *
 * @author- Vinay Panwar
 */
public interface SubMenuRepository extends JpaRepository<SubMenu, Long> {
	
	SubMenu findBySubMenuName(String subMenuName);
}
