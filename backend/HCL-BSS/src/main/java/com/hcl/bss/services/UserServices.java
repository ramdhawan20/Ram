package com.hcl.bss.services;

import com.hcl.bss.domain.User;
import com.hcl.bss.dto.UserInDto;

import java.util.List;

import org.springframework.data.domain.Pageable;
/**
 * This is interface to UserServicesImpl
 *
 * @author- Vinay Panwar
 */
public interface UserServices {
    User findById(int id);
    List<User> findByUserFirstName(String name) throws Exception;
    
	User findByUserId(String userId) throws Exception;
	
	List<User> findAllUser(UserInDto userIn, Pageable pageable) throws Exception;
    
    User addUser(User user) throws Exception ;
    
    User editUser(User user) throws Exception;
    
    User activateUser(User user) throws Exception;
    
    User resetUser(User user) throws Exception;
    
	List<String> getDropDownList(String dropDownCode);

}
