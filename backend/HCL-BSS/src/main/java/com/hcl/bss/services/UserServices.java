package com.hcl.bss.services;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.hcl.bss.domain.Role;
import com.hcl.bss.domain.RoleMenuMapping;
import com.hcl.bss.domain.User;
import com.hcl.bss.dto.MenuDto;
import com.hcl.bss.dto.ProfileInDto;
import com.hcl.bss.dto.RoleInDto;
import com.hcl.bss.dto.UserAuthDto;
import com.hcl.bss.dto.UserInDto;
/**
 * This is interface to UserServicesImpl
 *
 * @author- Vinay Panwar
 */
public interface UserServices {
    User findById(int id);
    List<User> findByUserFirstName(String name) throws Exception;
    
	User findByUserId(String userId) throws Exception;
	
	List<User> findAllUser(UserInDto userIn, Pageable pageable, HttpServletResponse response) throws Exception;
    
    User addUser(User user) throws Exception ;
    
    User editUser(User user) throws Exception;
    
    User activateUser(User user) throws Exception;
    
    User resetUser(User user) throws Exception;
    
	List<String> getDropDownList(String dropDownCode);

	UserAuthDto getAuthorizationDetail(String userId);
	
	List<String> getAllRoleName();
	
	Role addRole(Role role);
	
	MenuDto getAllMenu(String roleName);
	
	RoleMenuMapping createRoleMenuMapping(ProfileInDto profileInDto);
	
	void deleteRoleMenuMapping(RoleInDto roleIn);
	
	RoleMenuMapping updateRoleMenuMapping(ProfileInDto profileInDto);
	
}
