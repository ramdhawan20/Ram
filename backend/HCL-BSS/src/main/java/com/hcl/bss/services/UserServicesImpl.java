package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.ACTIVE;
import static com.hcl.bss.constants.ApplicationConstants.ADMIN;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.Menu;
import com.hcl.bss.domain.Role;
import com.hcl.bss.domain.SubMenu;
import com.hcl.bss.domain.User;
import com.hcl.bss.dto.MenuAuthDto;
import com.hcl.bss.dto.SubMenuAuthDto;
import com.hcl.bss.dto.UserAuthDto;
import com.hcl.bss.dto.UserInDto;
import com.hcl.bss.exceptions.CustomUserMgmtException;
import com.hcl.bss.repository.AppConstantRepository;
import com.hcl.bss.repository.UserRepository;
import com.hcl.bss.repository.specification.UserManagementSpecification;;
/**
 * This is UserServicesImpl will handle calls related to UserManagement
 *
 * @author- Vinay Panwar
 */
@Service
@Transactional
public class UserServicesImpl implements UserServices {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);
    @Autowired
    private UserRepository userRepository;
	@Autowired
	AppConstantRepository appConstantRepository;


    @Override
    public User findById(int id) {
		LOGGER.info("<-----------------------Start findById() method in UserServicesImpl-------------------------------->");		
        try {
			return userRepository.findById(id);
		} finally {
			LOGGER.info("<-----------------------End findById() method in UserServicesImpl-------------------------------->");		
		}
    }

 	@Override
    public User findByUserId(String userId) throws Exception {
		LOGGER.info("<-----------------------Start findByUserId() method in UserServicesImpl-------------------------------->");		
        try {
			return userRepository.findByUserId(userId);
		} finally {
			LOGGER.info("<-----------------------End findByUserId() method in UserServicesImpl-------------------------------->");		
		}
    }

    @Override
    public List<User> findByUserFirstName(String name) throws Exception {
		LOGGER.info("<-----------------------Start findByUserFirstName() method in UserServicesImpl-------------------------------->");		
        try {
			return userRepository.findByUserFirstName(name);
		} finally {
			LOGGER.info("<-----------------------End findByUserFirstName() method in UserServicesImpl-------------------------------->");		
		}
    }
    
	@Override
	public List<User> findAllUser(UserInDto userIn, Pageable pageable) throws Exception {
		LOGGER.info("<-----------------------Start findAllUser() method in UserServicesImpl-------------------------------->");	
		List<User> userList = null;
		int isLocked = -1;
		int roleId = 0;
		
		if(userIn.getStatus()!= null) {
			if(!"".equalsIgnoreCase(userIn.getStatus().trim())) {
				isLocked = userIn.getStatus().equalsIgnoreCase(ACTIVE) ? 0 : 1;
			}
		}
		if(userIn.getUserProfile()!= null) {
			if(!"".equalsIgnoreCase(userIn.getUserProfile().trim())) {
				roleId = userIn.getUserProfile().equalsIgnoreCase(ADMIN) ? 1 : 2;
			}
		}
			
		try {
			/*Start:- Defining specification for filter */
			Specification<User> specUserId = (userIn.getUserId() != null
					&& !"".equalsIgnoreCase(userIn.getUserId())
							? Specification.where(
									UserManagementSpecification.hasUserId(userIn.getUserId()))
							: null);
			Specification<User> specUserName = (userIn.getUserFirstName() != null
					&& !"".equalsIgnoreCase(userIn.getUserFirstName())
							? Specification.where(
									UserManagementSpecification.hasUserName(userIn.getUserFirstName()))
							: null);
			Specification<User> specStatus = (isLocked != -1 ? 
					Specification.where(UserManagementSpecification.isLocked(isLocked))
							: null);
			Specification<User> specRole = (roleId != 0 ? 
					Specification.where(UserManagementSpecification.hasRoleId(roleId))
							: null);
			
			/*End:- Defining specification for filter */
			
			//return userRepository.findAll();
			
			Page<User> userPages = userRepository.findAll(Specification.where(specUserId).and(specUserName).and(specStatus).and(specRole), pageable);
			
			userList = userPages.getContent();

			if (userList != null && userList.size() > 0) {
				userIn.setTotalPages(userPages.getTotalPages());
				userIn.setLastPage(userPages.isLast());
				return userList;
			}
			return null;
			
		} finally {
			LOGGER.info("<-----------------------End findAllUser() method in UserServicesImpl-------------------------------->");		
		}
	}
    
	@Override
	public User addUser(User user) throws Exception {
		LOGGER.info("<-----------------------Start addUser() method in UserServicesImpl-------------------------------->");		
		try {
			return userRepository.save(user);
		} finally {
			LOGGER.info("<-----------------------End addUser() method in UserServicesImpl-------------------------------->");		
		}
	}
	
	@Override
	public User editUser(User user) throws Exception {
		LOGGER.info("<-----------------------Start editUser() method in UserServicesImpl-------------------------------->");	
		User fetchUser = null;
		try {
			//fetch the user which you want to update
			fetchUser = userRepository.findByUserId(user.getUserId());
			
			if (user.getUserFirstName() != null && !"".equalsIgnoreCase(user.getUserFirstName())) {
				fetchUser.setUserFirstName(user.getUserFirstName());
			}
			if (user.getUserMiddleName() != null && !"".equalsIgnoreCase(user.getUserMiddleName())) {
				fetchUser.setUserMiddleName(user.getUserMiddleName());
			}
			if (user.getUserLastName() != null && !"".equalsIgnoreCase(user.getUserLastName())) {
				fetchUser.setUserLastName(user.getUserLastName());
			}
			if (user.getRoleId() != -1) {
				fetchUser.setRoleId(user.getRoleId());
			}
			
			return this.userRepository.save(fetchUser);
			
		} finally {
			fetchUser = null;
			LOGGER.info("<-----------------------End editUser() method in UserServicesImpl-------------------------------->");		
		}
	}
	
	/*Method used to activate and deactivate user*/
	@Override
	public User activateUser(User user) throws Exception {
		LOGGER.info("<-----------------------Start activateUser() method in UserServicesImpl-------------------------------->");	
		User fetchUser = null;
		
		try {
			//fetch the user which you want to update
			fetchUser = userRepository.findByUserId(user.getUserId());
			
			if (fetchUser.getIsLocked() == 0) {
				fetchUser.setIsLocked(1);
			} else {
				fetchUser.setIsLocked(0);
			}
			
			return this.userRepository.save(fetchUser);
			
		} finally {
			fetchUser = null;
			LOGGER.info("<-----------------------End activateUser() method in UserServicesImpl-------------------------------->");		
		}
	}
	
	@Override
	public User resetUser(User user) throws Exception {
		LOGGER.info("<-----------------------Start resetUser() method in UserServicesImpl-------------------------------->");		
		User fetchUser = null;
		try {
			//fetch the user which you want to update
			fetchUser = userRepository.findByUserId(user.getUserId());
			
			if (user.getPassword() != null && !"".equalsIgnoreCase(user.getPassword())) {
				fetchUser.setPassword(user.getPassword());
			}
			
			return this.userRepository.save(fetchUser);
			
		} finally {
			fetchUser = null;
			LOGGER.info("<-----------------------End resetUser() method in UserServicesImpl-------------------------------->");		
		}
	}
	
	@Override
	public List<String> getDropDownList(String dropDownCode){
		LOGGER.info("<-----------------------Start getDropDownData() method in UserServicesImpl-------------------------------->");		
		try {
			return appConstantRepository.findByAppConstantCode(dropDownCode);
		} finally {
			LOGGER.info("<-----------------------End getDropDownData() method in UserServicesImpl-------------------------------->");		
		}
	}
	
	@Override
	public UserAuthDto getAuthorizationDetail(String userId) {
		LOGGER.info("<-----------------------Start getAuthorizationDetail() method in UserServicesImpl-------------------------------->");		
		User user = null;
		user = userRepository.findByUserId(userId);
		
		if(user == null) {
			throw new CustomUserMgmtException(100);
		}
		
		LOGGER.info("<-----------------------End getAuthorizationDetail() method in UserServicesImpl-------------------------------->");		
		
		return convertUserToDto(user);
	}

	private UserAuthDto convertUserToDto(User user){
		LOGGER.info("<-----------------------Start convertUserToDto() method in UserServicesImpl-------------------------------->");		
		UserAuthDto userAuthDto = new UserAuthDto();
		List<MenuAuthDto> tempManuList = new ArrayList();
		List<SubMenuAuthDto> tempSubManuList = null;
		MenuAuthDto menuAuthDto = null;
		SubMenuAuthDto subMenuAuthDto = null;
		
		userAuthDto.setUserId(user.getUserId());
		userAuthDto.setUserFirstName(user.getUserFirstName());
		
		List<Role> roleList = user.getRoleList();		
		if(roleList == null) {
			throw new CustomUserMgmtException(103);
		}
		
		for(Role role : roleList) {
			Set<Menu> menuSet = role.getMenuSet();			
			if(menuSet == null) {
				throw new CustomUserMgmtException(101);
			}
			
			Iterator<Menu> menuItr = menuSet.iterator();
			while(menuItr.hasNext()) {
				Menu menu = menuItr.next();

				menuAuthDto = new MenuAuthDto();
				menuAuthDto.setMenuName(menu.getMenuName());
				
				List<SubMenu> subMenuList = menu.getSubMenu();
				tempSubManuList = new ArrayList();
				
				for(SubMenu subMenu : subMenuList) {
					subMenuAuthDto = new SubMenuAuthDto();
					
					subMenuAuthDto.setSubMenuName(subMenu.getSubMenuName());
					
					tempSubManuList.add(subMenuAuthDto);
					subMenuAuthDto = null;
				}
				menuAuthDto.setSubManuList(tempSubManuList);
				tempSubManuList = null;
				
				tempManuList.add(menuAuthDto);
				menuAuthDto = null;
			}
			
		}
		userAuthDto.setMenuList(tempManuList);
		
		LOGGER.info("<-----------------------End convertUserToDto() method in UserServicesImpl-------------------------------->");		
		return userAuthDto;
	}

}