package com.hcl.bss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.bss.domain.Menu;
/**
 *
 * @author- Vinay Panwar
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
/*	 @Query("SELECT menu.menuName, submenu.subMenuName FROM Menu menu left join SubMenu submenu on menu.id =  submenu.menuUid")
	  Map<String, List<String>> getAllMenuSubMenu();
*/
	
	Menu findByMenuName(String menuName);
	
	Menu findByid(Long id);
}
 