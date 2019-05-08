package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.ACTIVE;
import static com.hcl.bss.constants.ApplicationConstants.ADMIN;
import static com.hcl.bss.constants.ApplicationConstants.APOSTROPHE;
import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.SPACE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
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
import com.hcl.bss.domain.RoleMenuMapping;
import com.hcl.bss.domain.SubMenu;
import com.hcl.bss.domain.User;
import com.hcl.bss.domain.UserRoleMapping;
import com.hcl.bss.dto.MenuDto;
import com.hcl.bss.dto.ProfileInDto;
import com.hcl.bss.dto.RoleInDto;
import com.hcl.bss.dto.RoleMenuDto;
import com.hcl.bss.dto.UserAuthDto;
import com.hcl.bss.dto.UserInDto;
import com.hcl.bss.dto.UserInputDto;
import com.hcl.bss.exceptions.CustomSubscriptionException;
import com.hcl.bss.exceptions.CustomUserMgmtException;
import com.hcl.bss.repository.AppConstantRepository;
import com.hcl.bss.repository.MenuRepository;
import com.hcl.bss.repository.RoleMenuMappingRepository;
import com.hcl.bss.repository.RoleRepository;
import com.hcl.bss.repository.SubMenuRepository;
import com.hcl.bss.repository.UserRepository;
import com.hcl.bss.repository.UserRoleMappingRepository;
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
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private SubMenuRepository subMenuRepository;
    @Autowired
    private RoleMenuMappingRepository roleMenuMappingRepository;
    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

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
	public List<User> findAllUser(UserInDto userIn, Pageable pageable, HttpServletResponse response) throws Exception {
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
			if(null != pageable) {
			Page<User> userPages = userRepository.findAll(Specification.where(specUserId).and(specUserName).and(specStatus).and(specRole), pageable);
			
			userList = userPages.getContent();

			if (userList != null && userList.size() > 0) {
				userIn.setTotalPages(userPages.getTotalPages());
				userIn.setLastPage(userPages.isLast());
				return userList;
			}
			}
			else {
				userList = userRepository.findAll(Specification.where(specUserId).and(specUserName).and(specStatus).and(specRole));
				return userList;
			}
			
			return null;
			
		} finally {
			LOGGER.info("<-----------------------End findAllUser() method in UserServicesImpl-------------------------------->");		
		}
	}
    
	@Override
	public User addUser(UserInputDto userInput) {
		LOGGER.info("<-----------------------Start addUser() method in UserServicesImpl-------------------------------->");		
		User user = new User();
		Set<String> userProfileSet = null;
		List<Role> roleList = new ArrayList();
		try {
			user.setUserId(userInput.getUserId());
			if(userInput.getUserFirstName() == null || userInput.getUserFirstName().trim().isEmpty())
				throw new CustomUserMgmtException(400, "UserFirstName can not be null/blank!");	
			
			user.setUserFirstName(userInput.getUserFirstName());
			user.setUserMiddleName(userInput.getUserMiddleName());
			user.setUserLastName(userInput.getUserLastName());
			
			userProfileSet = userInput.getUserProfileSet();
			if(userProfileSet == null || userProfileSet.size() == 0)
				throw new CustomUserMgmtException(400, "User should belong to atleast one profile!");	
			
			for(String roleName : userProfileSet) {
				Role role = roleRepository.findByRoleName(roleName.trim());
				
				if(role == null)
					throw new CustomUserMgmtException(400, APOSTROPHE + roleName.trim() + "' profile does not exist, please provide the correct profile!");
				
				roleList.add(role);
			}
			//user.setRoleId(userInput.getUserProfile().trim().equalsIgnoreCase(ADMIN) ? ROLE_ADMIN : ROLE_NORMAL);	
			user.setPassword(userInput.getAttribute());
			
			User createdUser = userRepository.save(user);
			if(createdUser == null) 
				throw new CustomUserMgmtException(400, "User not created!");	
			
			for(Role role : roleList) {
				UserRoleMapping userRoleMapping = new UserRoleMapping();
				userRoleMapping.setUserId(createdUser.getUserId());
				userRoleMapping.setRoleUid(role.getId());
				
				userRoleMappingRepository.save(userRoleMapping);
			}

			return null;
			
		} finally {
			LOGGER.info("<-----------------------End addUser() method in UserServicesImpl-------------------------------->");		
		}
	}
	
	@Override
	public UserInputDto editUser(UserInputDto userInput) {
		LOGGER.info("<-----------------------Start editUser() method in UserServicesImpl-------------------------------->");	
		User fetchedUser = null;
		Set<String> userProfileSet = null;
		
		try {
			//fetch the user which you want to update
			fetchedUser = userRepository.findByUserId(userInput.getUserId());
			if(fetchedUser == null)
				throw new CustomUserMgmtException(112, "User not found!");	
			if(fetchedUser.getIsLocked() == 1)
				throw new CustomUserMgmtException(113, "User is deactivated, can't be updated!");	
				
			userProfileSet = userInput.getUserProfileSet();
			if(userProfileSet != null && userProfileSet.size() > 0) {
				
				List<UserRoleMapping> userRoleMappingList = userRoleMappingRepository.findByUserId(fetchedUser.getUserId());
				if(userRoleMappingList == null || userRoleMappingList.size()==0) {
					throw new CustomUserMgmtException(110, "User profile mapping not found");	
				}
				userRoleMappingRepository.deleteAll(userRoleMappingList);
				
				for(String roleName : userProfileSet) {
					UserRoleMapping userRoleMapping = new UserRoleMapping();
					userRoleMapping.setUserId(fetchedUser.getUserId());
					Long roleId = roleRepository.findByRoleName(roleName.trim()).getId();
					userRoleMapping.setRoleUid(roleId);
					
					userRoleMappingRepository.save(userRoleMapping);
				}
			}
			
			if (userInput.getUserFirstName() != null && !BLANK.equalsIgnoreCase(userInput.getUserFirstName().trim())) {
				fetchedUser.setUserFirstName(userInput.getUserFirstName());
			}
			if (userInput.getUserMiddleName() != null && !BLANK.equalsIgnoreCase(userInput.getUserMiddleName().trim())) {
				fetchedUser.setUserMiddleName(userInput.getUserMiddleName());
			}
			if (userInput.getUserLastName() != null && !BLANK.equalsIgnoreCase(userInput.getUserLastName().trim())) {
				fetchedUser.setUserLastName(userInput.getUserLastName());
			}
			userRepository.save(fetchedUser);
			
			return null; 
		} finally {
			fetchedUser = null;
			userProfileSet = null;
			userInput = null;
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
			throw new CustomUserMgmtException(100, "No User Found");
		}
		
		LOGGER.info("<-----------------------End getAuthorizationDetail() method in UserServicesImpl-------------------------------->");		
		
		return convertUserToDto(user);
	}

	private UserAuthDto convertUserToDto(User user) {
		LOGGER.info("<-----------------------Start convertUserToDto() method in UserServicesImpl-------------------------------->");		
		UserAuthDto userAuthDto = new UserAuthDto();
		Map<String, Set<String>> menuMap = new HashMap<>();
		Set<String> subMenuSet = null;
		Set<String> roleNameSet = new HashSet<>();
		Menu menu = null;
		SubMenu subMenu = null;
		
		userAuthDto.setUserId(user.getUserId());
		StringBuilder userNameSB = new StringBuilder();
		userNameSB.append(user.getUserFirstName())
		.append(user.getUserMiddleName() != null && !BLANK.equals(user.getUserMiddleName().trim()) ? SPACE+user.getUserMiddleName() : BLANK)
		.append(user.getUserLastName() != null && !BLANK.equals(user.getUserLastName().trim()) ? SPACE+user.getUserLastName() : BLANK);

		//userNameSB.append(user.getUserFirstName()).append(user.getUserMiddleName()).append(user.getUserLastName());
		
		userAuthDto.setUserName(userNameSB.toString());
	
		List<Role> roleList = user.getRoleList();		
		if(roleList == null) {
			throw new CustomUserMgmtException(103, "Role is not associated with this user");
		}
		
		for(Role role : roleList) {
			List<RoleMenuMapping> roleMenuMappingList = roleMenuMappingRepository.findByRoleUid(role.getId());
			roleNameSet.add(role.getRoleName());
			if(roleMenuMappingList == null) {
				throw new CustomUserMgmtException(106, "Profile mapping failed");
			}
			
			for(RoleMenuMapping roleMenuMapping : roleMenuMappingList){
				menu = menuRepository.findByid(roleMenuMapping.getMenuUid());
				subMenu = subMenuRepository.findByid(roleMenuMapping.getSubMenuUid());
				subMenuSet = new HashSet<>();
				
				if(menuMap.containsKey(menu.getMenuName())) {
					subMenuSet = menuMap.get(menu.getMenuName());
					if(subMenu != null)
						subMenuSet.add(subMenu.getSubMenuName());
					menuMap.put(menu.getMenuName(), subMenuSet);
				}else {
					menuMap.put(menu.getMenuName(), null);
					if(subMenu != null)
						subMenuSet.add(subMenu.getSubMenuName());
					else
						subMenuSet = new HashSet<>();
					
					menuMap.put(menu.getMenuName(), subMenuSet);
				}
				subMenuSet = null;
				menu = null;
				subMenu = null;
			}
			
		}
		userAuthDto.setRoleNameSet(roleNameSet);
		userAuthDto.setMenuMap(menuMap);
		userNameSB = null;
		LOGGER.info("<-----------------------End convertUserToDto() method in UserServicesImpl-------------------------------->");		
		return userAuthDto;
	}
	
	@Override
	public List<String> getAllRoleName(){
		LOGGER.info("<-----------------------Start getAllRoleName() method in UserServicesImpl-------------------------------->");	
		List<String> roleNameList = null;
		
		roleNameList = roleRepository.getAllRoleName();
		
		if(roleNameList == null) {
			throw new CustomUserMgmtException(104, "No profile found");			
		}

		LOGGER.info("<-----------------------End getAllRoleName() method in UserServicesImpl-------------------------------->");		
		
		return roleNameList;
	}

	@Override
	public Role addRole(Role role) {
		LOGGER.info("<-----------------------Start addRole() method in UserServicesImpl-------------------------------->");		
		
		Role createdRole = roleRepository.save(role);
		if(createdRole == null) {
			throw new CustomUserMgmtException(105, "Profile creation failed");			
		}
		LOGGER.info("<-----------------------End addRole() method in UserServicesImpl-------------------------------->");		
		
		return createdRole;
	}

	@Override
	public MenuDto getAllMenu(String roleName){
		LOGGER.info("<-----------------------Start getAllMenu() method in UserServicesImpl-------------------------------->");	
		MenuDto menuDto = new MenuDto();
		List<Menu> menuList = null;
		
		menuList = menuRepository.findAll();
		
		if(menuList == null) {
			throw new CustomUserMgmtException(104, "No profile found");			
		}

		LOGGER.info("<-----------------------End getAllMenu() method in UserServicesImpl-------------------------------->");		
		
		return convertToMenuDto(menuList, roleName);
	}
	
    private MenuDto convertToMenuDto(List<Menu> menuList, String roleName) {
		LOGGER.info("<-----------------------Start convertUserToDto() method in UserServicesImpl-------------------------------->");		
    	MenuDto menuDto = new MenuDto();
    	Map<String, Set<String>> mappedMenuMap = new HashMap<>();
    	Map<String, Set<String>> unmappedMenuMap = new HashMap<>();
		Set<String> subMenuSet = null;
		Menu menu = null;
		SubMenu subMenu = null;

		//To fetch all menu & sub-menu's exists in the system
		for(Menu mmenu : menuList) {
			subMenuSet = new HashSet();
			List<SubMenu> subMenuList = mmenu.getSubMenu();

			if(subMenuList != null) {
				for(SubMenu ssubMenu : subMenuList) {
					subMenuSet.add(ssubMenu.getSubMenuName());
				}
			}
			unmappedMenuMap.put(mmenu.getMenuName(), subMenuSet);
			subMenuSet = null;
		}
		menuDto.setUnmappedMenuMap(unmappedMenuMap);   	

		if(roleName == null || "".equalsIgnoreCase(roleName.trim())) {
			return menuDto;
		}
		
		//To fetch all menu & sub-menu's which are mapped with specific role
		Role role = roleRepository.findByRoleName(roleName);
		if(role == null) {
			throw new CustomUserMgmtException(107, "Role Name not found");	
		}
		
		menuDto.setRoleName(role.getRoleName());

		List<RoleMenuMapping> roleMenuMappingList = roleMenuMappingRepository.findByRoleUid(role.getId());
		if(roleMenuMappingList == null) {
			throw new CustomUserMgmtException(106, "Profile mapping failed");
		}
		
		for(RoleMenuMapping roleMenuMapping : roleMenuMappingList){
			menu = menuRepository.findByid(roleMenuMapping.getMenuUid());
			subMenu = subMenuRepository.findByid(roleMenuMapping.getSubMenuUid());
			subMenuSet = new HashSet<>();
			
			if(mappedMenuMap.containsKey(menu.getMenuName())) {
				subMenuSet = mappedMenuMap.get(menu.getMenuName());
				if(subMenu != null)
					subMenuSet.add(subMenu.getSubMenuName());
				mappedMenuMap.put(menu.getMenuName(), subMenuSet);
			}else {
				mappedMenuMap.put(menu.getMenuName(), null);
				if(subMenu != null)
					subMenuSet.add(subMenu.getSubMenuName());
				else
					subMenuSet = new HashSet<>();
				
				mappedMenuMap.put(menu.getMenuName(), subMenuSet);
			}
			subMenuSet = null;
			menu = null;
			subMenu = null;
		}
		menuDto.setMappedMenuMap(mappedMenuMap);
		
		//To fetch all menu & sub-menu's which are not mapped with specific role
		menuDto.setUnmappedMenuMap(removeMappedFromUnmapped(mappedMenuMap, unmappedMenuMap));
		
		LOGGER.info("<-----------------------End convertUserToDto() method in UserServicesImpl-------------------------------->");		

		return menuDto;
	}
    
    private Map<String, Set<String>> removeMappedFromUnmapped(Map<String, Set<String>> mappedMenuMap, Map<String, Set<String>> unmappedMenuMap) {
		LOGGER.info("<-----------------------Start removeMappedFromUnmapped() method in UserServicesImpl-------------------------------->");		
    	Set<String> mappedKeySet = mappedMenuMap.keySet();
    	for(String key : mappedKeySet) {
    		if(unmappedMenuMap.containsKey(key)) {
    			Set<String> unmapSet = unmappedMenuMap.get(key);
    			Set<String> mapSet = mappedMenuMap.get(key);
    			//if(unmapSet.size() == mapSet.size()) {
       			if(unmapSet.equals(mapSet)) {
    				unmappedMenuMap.remove(key);
    			}else {
    				unmapSet.removeAll(mapSet);
    				unmappedMenuMap.put(key, unmapSet);
    			}
    			
    		}
    	}
    	
		LOGGER.info("<-----------------------Start removeMappedFromUnmapped() method in UserServicesImpl-------------------------------->");		
    	return unmappedMenuMap;
    }
    
	
	@Override
	public RoleMenuMapping createRoleMenuMapping(ProfileInDto profileInDto) {
		LOGGER.info("<-----------------------Start roleMenuMapping() method in UserServicesImpl-------------------------------->");		
		Role checkRole = roleRepository.findByRoleName(profileInDto.getRoleName());
		if(checkRole != null)
			throw new CustomUserMgmtException(111, "Profile is already not exists");	
		
		Role newRole = new Role();
		newRole.setRoleName(profileInDto.getRoleName());
		newRole.setDescription(profileInDto.getDescription());
		
		checkRole = roleRepository.save(newRole);
		if(checkRole == null) {
			throw new CustomUserMgmtException(105, "Profile creation failed");			
		}
		
		long roleId = checkRole.getId();
/*		if(roleId == 0) {
			throw new CustomUserMgmtException(107);	
		}
*/		
		List<RoleMenuDto> roleMenuDtoList = profileInDto.getMenuList();
		
		for(RoleMenuDto roleMenuDto : roleMenuDtoList) {
			String menuName = roleMenuDto.getMenuName();
			long menuId = menuRepository.findByMenuName(roleMenuDto.getMenuName()).getId();
			if(menuId == 0) {
				throw new CustomUserMgmtException(108, "Menu Name not found");	
			}
			
			List<String> subMenuNameList = roleMenuDto.getSubMenuList();
			RoleMenuMapping roleMenuMapping = null;
			
			if(subMenuNameList != null && subMenuNameList.size() > 0) {
				//if(subMenuNameList.size() > 0) {
					for(String subMenuName : subMenuNameList) {
						long subMenuId = subMenuRepository.findBySubMenuName(subMenuName).getId();
						if(subMenuId == 0) {
							throw new CustomUserMgmtException(109, "Sub Menu Name not found");	
						}
						roleMenuMapping = new RoleMenuMapping();
						roleMenuMapping.setRoleUid(roleId);
						roleMenuMapping.setMenuUid(menuId);
						roleMenuMapping.setSubMenuUid(subMenuId);
						
						RoleMenuMapping roleMenuMap = roleMenuMappingRepository.save(roleMenuMapping);
						if(roleMenuMap == null) {
							throw new CustomUserMgmtException(106, "Profile mapping failed");	
						}
						roleMenuMapping = null;
					}
					
/*				}else {
					roleMenuMapping = new RoleMenuMapping();
					roleMenuMapping.setRoleUid(roleId);
					roleMenuMapping.setMenuUid(menuId);
					
					RoleMenuMapping roleMenuMap = roleMenuMappingRepository.save(roleMenuMapping);
					if(roleMenuMap == null) {
						throw new CustomUserMgmtException(106, "Profile mapping failed");	
					}
					roleMenuMapping = null;
				}
*/			}else {
				roleMenuMapping = new RoleMenuMapping();
				roleMenuMapping.setRoleUid(roleId);
				roleMenuMapping.setMenuUid(menuId);
				
				RoleMenuMapping roleMenuMap = roleMenuMappingRepository.save(roleMenuMapping);
				if(roleMenuMap == null) {
					throw new CustomUserMgmtException(106, "Profile mapping failed");	
				}
				roleMenuMapping = null;
			}
			
		}
		
		LOGGER.info("<-----------------------End roleMenuMapping() method in UserServicesImpl-------------------------------->");		

		return null;
	}

	@Override
	public void deleteRoleMenuMapping(RoleInDto roleIn) {
		LOGGER.info("<-----------------------Start deleteRoleMenuMapping() method in UserServicesImpl-------------------------------->");
		long roleId = roleRepository.findByRoleName(roleIn.getRoleName()).getId();
		if(roleId == 0) {
			throw new CustomUserMgmtException(104, "No profile found");			
		}
		List<UserRoleMapping> userRoleMappingList = userRoleMappingRepository.findByRoleUid(roleId);
		
		if(userRoleMappingList != null && userRoleMappingList.size() > 0)
			//throw new CustomUserMgmtException(112);	
			throw new CustomSubscriptionException(100, "Profile already mapped with existing user can't be deleted");
		
		List<RoleMenuMapping> roleMenuMappingList = roleMenuMappingRepository.findByRoleUid(roleId);
		if(roleMenuMappingList == null) {
			throw new CustomUserMgmtException(110, "Profile mapping not found");			
		}
		
		roleMenuMappingRepository.deleteAll(roleMenuMappingList);
		
		roleRepository.deleteById(roleId);
		
		LOGGER.info("<-----------------------End deleteRoleMenuMapping() method in UserServicesImpl-------------------------------->");		
	}

	@Override
	public RoleMenuMapping updateRoleMenuMapping(ProfileInDto profileInDto) {
		LOGGER.info("<-----------------------Start updateRoleMenuMapping() method in UserServicesImpl-------------------------------->");		
		long roleId = roleRepository.findByRoleName(profileInDto.getRoleName()).getId();
		if(roleId == 0) {
			throw new CustomUserMgmtException(107, "Role Name not found");	
		}
		List<RoleMenuMapping> roleMenuMappingList = roleMenuMappingRepository.findByRoleUid(roleId);
		if(roleMenuMappingList == null || roleMenuMappingList.size()==0) {
			throw new CustomUserMgmtException(110, "Profile mapping not found");	
		}
		roleMenuMappingRepository.deleteAll(roleMenuMappingList);
		
		List<RoleMenuDto> roleMenuDtoList = profileInDto.getMenuList();
		
		for(RoleMenuDto roleMenuDto : roleMenuDtoList) {
			String menuName = roleMenuDto.getMenuName();
			long menuId = menuRepository.findByMenuName(roleMenuDto.getMenuName()).getId();
			if(menuId == 0) {
				throw new CustomUserMgmtException(108, "Menu Name not found");	
			}
			
			List<String> subMenuNameList = roleMenuDto.getSubMenuList();
			RoleMenuMapping roleMenuMapping = null;
			
			if(subMenuNameList != null && subMenuNameList.size() > 0) {
				//if(subMenuNameList.size() > 0) {
					for(String subMenuName : subMenuNameList) {
						long subMenuId = subMenuRepository.findBySubMenuName(subMenuName).getId();
						if(subMenuId == 0) {
							throw new CustomUserMgmtException(109, "Sub Menu Name not found");	
						}
						roleMenuMapping = new RoleMenuMapping();
						roleMenuMapping.setRoleUid(roleId);
						roleMenuMapping.setMenuUid(menuId);
						roleMenuMapping.setSubMenuUid(subMenuId);
						
						RoleMenuMapping roleMenuMap = roleMenuMappingRepository.save(roleMenuMapping);
						if(roleMenuMap == null) {
							throw new CustomUserMgmtException(106, "Profile mapping failed");	
						}
						roleMenuMapping = null;
					}
					
/*				}else {
					roleMenuMapping = new RoleMenuMapping();
					roleMenuMapping.setRoleUid(roleId);
					roleMenuMapping.setMenuUid(menuId);
					
					RoleMenuMapping roleMenuMap = roleMenuMappingRepository.save(roleMenuMapping);
					if(roleMenuMap == null) {
						throw new CustomUserMgmtException(106, "Profile mapping failed");	
					}
					roleMenuMapping = null;
				}
*/			}else {
				roleMenuMapping = new RoleMenuMapping();
				roleMenuMapping.setRoleUid(roleId);
				roleMenuMapping.setMenuUid(menuId);
				
				RoleMenuMapping roleMenuMap = roleMenuMappingRepository.save(roleMenuMapping);
				if(roleMenuMap == null) {
					throw new CustomUserMgmtException(106, "Profile mapping failed");	
				}
				roleMenuMapping = null;
			}
			
		}
		
		LOGGER.info("<-----------------------End updateRoleMenuMapping() method in UserServicesImpl-------------------------------->");		

		return null;
	}
	
/*	public boolean isRoleExist(String roleName){
		Role role = roleRepository.findByRoleName(roleName.trim());
		if(role == null)
			return false;
		
		return true;
	}
*/	

  @Override
	public List<RoleInDto> getAllRoles(Pageable pageable) {
		List<Role> roleList = roleRepository.findAll(pageable).getContent();
		if(roleList!=null && !roleList.isEmpty()) {
			List<RoleInDto> roleDtoList = new ArrayList<RoleInDto>();
			for(Role role: roleList) {
				RoleInDto roleDto = new RoleInDto();
				roleDto.setRoleName(role.getRoleName());
				roleDto.setDescription(role.getDescription());
				roleDtoList.add(roleDto);
			}
			return roleDtoList;
		}
		else
			throw new CustomUserMgmtException(100, "No role found");
	}
}