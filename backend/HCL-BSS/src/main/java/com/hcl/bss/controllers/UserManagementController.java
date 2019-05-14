package com.hcl.bss.controllers;

import static com.hcl.bss.constants.ApplicationConstants.ACTIVE;
import static com.hcl.bss.constants.ApplicationConstants.INACTIVE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.Role;
import com.hcl.bss.domain.User;
import com.hcl.bss.dto.DropDownOutDto;
import com.hcl.bss.dto.MenuDto;
import com.hcl.bss.dto.ProfileInDto;
import com.hcl.bss.dto.RoleInDto;
import com.hcl.bss.dto.RoleInputDto;
import com.hcl.bss.dto.RoleOutDto;
import com.hcl.bss.dto.RoleResponseDto;
import com.hcl.bss.dto.UserAuthDto;
import com.hcl.bss.dto.UserDto;
import com.hcl.bss.dto.UserInDto;
import com.hcl.bss.dto.UserInputDto;
import com.hcl.bss.dto.UserOutDto;
import com.hcl.bss.exceptions.CustomUserMgmtException;
import com.hcl.bss.repository.RoleRepository;
import com.hcl.bss.repository.UserRepository;
import com.hcl.bss.services.UserServices;

import io.swagger.annotations.ApiOperation;;
/**
 * This is UserManagementController will handle calls related to UserManagement
 *
 * @author- Vinay Panwar
 */
@CrossOrigin(origins = "*")
@RestController
public class UserManagementController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);
	@Autowired
	UserServices userServices;
	@Autowired
	public UserRepository userRepository;
	@Autowired
	public RoleRepository roleRepository;
	@Value("${app.page.size}") String pageSize;

	//@Autowired
	//User user = new User();

/*	@ApiOperation(value = "Get user by id", response = UserDetails.class)
	@GetMapping(value = "/user")
	public ResponseEntity<?> findById(@RequestParam(value="userId", required = true) String userId) {
		User user = userServices.findById(userId);
		
		UserDetails userDetails = new UserDetails();
		userDetails.setUserProfile(user.getRoleId()== 0 ? "Admin" : "Normal");
		userDetails.setUserId(user.getUserId());
		userDetails.setUserFirstName(user.getUserFirstName());
		userDetails.setUserMiddleName(user.getUserMiddleName());
		userDetails.setUserLastName(user.getUserLastName());
		userDetails.setIsLocked(String.valueOf(user.getIsLocked()));
		userDetails.setMessage("User fetched successfully");
		
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}
*/	
	
	@ApiOperation(value = "Get list of all user", response = UserOutDto.class)
	@PutMapping(value = "/users/userlist")
	public ResponseEntity<?> findAllUser(@Valid @RequestBody UserInDto userIn, HttpServletResponse response) {
	//public ResponseEntity<?> findAllUser() {
		LOGGER.info("<-----------------------Start findAllUser() method-------------------------------->");
		LOGGER.info("I/P details: " + userIn.toString());
		UserDto userDto = null;
		List<UserDto> userListOut = new ArrayList<>();
		UserOutDto userOut = new UserOutDto();
		List<User> userList = null;
		Set<String> userProfileSet = null;
		//Pageable pageable = new PageRequest(userIn.getPageNo(), 1);
		Pageable pageable = PageRequest.of(userIn.getPageNo(), Integer.parseInt(pageSize));
		
		try {
			userList = userServices.findAllUser(userIn, pageable, response);
			
			if(userList == null && userList.size() == 0) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.NOT_FOUND.value());
				userOut.setMessage("No user found!");
				userOut.setUserList(null);

				return new ResponseEntity<>(userOut, HttpStatus.NOT_FOUND);
			}
			
			for(User usr : userList) {
				userDto = new UserDto();
				userProfileSet = new HashSet();
				//userDto.setUserProfile(usr.getRoleId()== ROLE_ADMIN ? ADMIN : NORMAL);
				List<Role> roleList = usr.getRoleList();
				for(Role role : roleList)
					userProfileSet.add(role.getRoleName());
				userDto.setUserProfileSet(userProfileSet);
				userDto.setUserId(usr.getUserId());
				userDto.setUserFirstName(usr.getUserFirstName());
				userDto.setUserMiddleName(usr.getUserMiddleName()==null ? "" : usr.getUserMiddleName());
				userDto.setUserLastName(usr.getUserLastName()==null ? "" : usr.getUserLastName());
				userDto.setStatus(usr.getIsLocked()==0 ? ACTIVE : INACTIVE);
				
				userListOut.add(userDto);
				userProfileSet = null;
			}
			
			userOut.setSuccess(true);
			userOut.setResponseCode(HttpStatus.OK.value());
			userOut.setMessage("All user fetched successfully!");
			userOut.setUserList(userListOut);
			userOut.setTotalPages(userIn.getTotalPages());
			userOut.setLastPage(userIn.isLastPage());
			
			return new ResponseEntity<>(userOut, HttpStatus.OK);
			
		} catch (Exception e) {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			userOut.setSuccess(false);
			userOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			return new ResponseEntity<>(userOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			LOGGER.info("<-----------------------Start finally block-------------------------------->");
			userDto = null;
			userListOut = null;
			userProfileSet = null;
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End findAllUser() method-------------------------------->");		
		}
		
	}
	
	@ApiOperation(value = "To create new user", response = UserOutDto.class)
	@PostMapping(value = "/users/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody  UserInputDto userInput) {
		LOGGER.info("<-----------------------Start addUser() method-------------------------------->");		
		LOGGER.info("Input details: " + userInput.toString());

		UserOutDto userOut = new UserOutDto();
		
		try {
			userServices.addUser(userInput);
			
			userOut.setSuccess(true);
			userOut.setResponseCode(HttpStatus.OK.value());
			userOut.setMessage("User created successfully!");
			return new ResponseEntity<>(userOut, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			userOut.setSuccess(false);
			userOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			return new ResponseEntity<>(userOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			LOGGER.info("<-----------------------End addUser() method-------------------------------->");		
		}
	}

	@ApiOperation(value = "To update existing user", response = UserOutDto.class)
	@PutMapping(value = "/users/user")
	public ResponseEntity<?> editUser(@Valid @RequestBody  UserInputDto userInput) {
		LOGGER.info("<-----------------------Start editUser() method-------------------------------->");		
		LOGGER.info("Input details: " + userInput.toString());

		UserOutDto userOut = new UserOutDto();
		
		try {
			userServices.editUser(userInput);
			
			userOut.setSuccess(true);
			userOut.setResponseCode(HttpStatus.OK.value());
			userOut.setMessage("User updated successfully!");
			return new ResponseEntity<>(userOut, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			userOut.setSuccess(false);
			userOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			return new ResponseEntity<>(userOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			LOGGER.info("<-----------------------End editUser() method-------------------------------->");		
		}
		
	}

	@ApiOperation(value = "To activate/deactivate existing user", response = UserOutDto.class)
	@PutMapping(value = "/users/activate")
	public ResponseEntity<?> activateUser(@Valid @RequestBody  UserInDto userIn) {
		LOGGER.info("<-----------------------Start activateUser() method-------------------------------->");		
		LOGGER.info("Input details: " + userIn.toString());

		User user = new User();
		User updatedUser = null;
		UserOutDto userOut = new UserOutDto();

		try {
			user.setUserId(userIn.getUserId());

			updatedUser = this.userServices.activateUser(user);
			
			if(updatedUser == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User is not activated/deactivated!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}

			if(updatedUser.getIsLocked() == 0) {
				userOut.setSuccess(true);
				userOut.setResponseCode(HttpStatus.OK.value());
				userOut.setMessage("User activated successfully!");
				return new ResponseEntity<>(userOut, HttpStatus.OK);
			}else {
				userOut.setSuccess(true);
				userOut.setResponseCode(HttpStatus.OK.value());
				userOut.setMessage("User deactivated successfully!");
				return new ResponseEntity<>(userOut, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			userOut.setSuccess(false);
			userOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			return new ResponseEntity<>(userOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			LOGGER.info("<-----------------------Start finally block-------------------------------->");
			user = null;
			updatedUser = null;
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End activateUser() method-------------------------------->");		
		}
		
	}

	@ApiOperation(value = "To reset user password", response = UserOutDto.class)
	@PutMapping(value = "/users/reset")
	public ResponseEntity<?> resetUser(@Valid @RequestBody  UserInDto userIn) {
		LOGGER.info("<-----------------------Start resetUser() method-------------------------------->");		
		LOGGER.info("Input details: " + userIn.toString());

		User user = new User();
		UserOutDto userOut = new UserOutDto();
		User updatedUser = null;

		try {
			user.setUserId(userIn.getUserId());
			
			if(userIn.getNewAttribute() == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("NewAttribute can not be null!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
			if("".equalsIgnoreCase(userIn.getNewAttribute().trim())) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("NewAttribute can not be blank!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			user.setPassword(userIn.getNewAttribute());

			updatedUser = this.userServices.resetUser(user);
			
			if(updatedUser == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("Password not changed!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			userOut.setSuccess(true);
			userOut.setResponseCode(HttpStatus.OK.value());
			userOut.setMessage("Password changed successfully!");
			return new ResponseEntity<>(userOut, HttpStatus.OK);
			

		} catch (Exception e) {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			userOut.setSuccess(false);
			userOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			return new ResponseEntity<>(userOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			LOGGER.info("<-----------------------Start finally block-------------------------------->");
			user = null;
			updatedUser = null;
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End resetUser() method-------------------------------->");		
		}
	}
	
	@ApiOperation(value = "To get dropdown lov", response = DropDownOutDto.class)
	@GetMapping(value = "/users/getDropDownList")
	public ResponseEntity<?> getDropDownList(@RequestParam(value = "dropDownCode", required=true) String dropDownCode) {
		LOGGER.info("<-----------------------Start getDropDownList() method in UserManagementController-------------------------------->");		
		List<String> dropDownList = null;
		DropDownOutDto dropDownOut = new DropDownOutDto();
		
		try {
			dropDownList = userServices.getDropDownList(dropDownCode);
			
			if (dropDownList == null || dropDownList.size() == 0) {
				dropDownOut.setSuccess(false);
				dropDownOut.setResponseCode(HttpStatus.NOT_FOUND.value());
				dropDownOut.setMessage("Dropdown lov not found!");

				return new ResponseEntity<>(dropDownOut, HttpStatus.NOT_FOUND);
			}
			
			dropDownOut.setSuccess(true);
			dropDownOut.setResponseCode(HttpStatus.OK.value());
			dropDownOut.setMessage("Dropdown lov fetched successfully!");
			dropDownOut.setDropDownList(dropDownList);
			return new ResponseEntity<>(dropDownOut, HttpStatus.OK);
			
		} catch (Exception e)  {
			LOGGER.info("<-----------------------Start catch block-------------------------------->");
			dropDownOut.setSuccess(false);
			dropDownOut.setMessage(e.getMessage());
			e.printStackTrace();
			LOGGER.info("Error Description: " + e.getMessage());
			LOGGER.info("<-----------------------End catch block-------------------------------->");
			
			return new ResponseEntity<>(dropDownOut, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			LOGGER.info("<-----------------------Start finally block-------------------------------->");
			dropDownList = null;
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End getDropDownList() method in UserManagementController-------------------------------->");		
		}
	}


	@ApiOperation(value = "To get user authorization details", response = UserAuthDto.class)
	@GetMapping(value = "/users/authorize")
	public ResponseEntity<UserAuthDto> getAuthorizationDetail(@RequestParam(value="userId", required = true) String userId){
		LOGGER.info("<-----------------------Start getAuthorizationDetail() method in UserManagementController-------------------------------->");		
		try {
			UserAuthDto userAuthDto = userServices.getAuthorizationDetail(userId);
			
			return new ResponseEntity<>(userAuthDto, HttpStatus.OK);
			
		} catch (CustomUserMgmtException e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500, e.getMessage());
		}finally {
			LOGGER.info("<-----------------------End getAuthorizationDetail() method in UserManagementController-------------------------------->");		
		}
		
	}

	@ApiOperation(value = "To get all profile name list", response = String.class)
	@GetMapping(value = "/users/profile")
	public ResponseEntity<List<String>> getAllRoleName(){
		LOGGER.info("<-----------------------Start getAllRoleName() method in UserManagementController-------------------------------->");		
		try {
			List<String> profileNameList = userServices.getAllRoleName();
			
			return new ResponseEntity<>(profileNameList, HttpStatus.OK);
			
		} catch (CustomUserMgmtException e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500, e.getMessage());
		}finally {
			LOGGER.info("<-----------------------End getAllRoleName() method in UserManagementController-------------------------------->");		
		}
		
	}
	
	@ApiOperation(value = "To get all role name and discription ist", response = String.class)
	@GetMapping(value = "/users/roles/{pageNo}", produces = {"application/json"})
	public ResponseEntity<?> getAllRole(@PathVariable("pageNo") String pageNo){
		LOGGER.info("<-----------------------Start getAllRole() method in UserManagementController-------------------------------->");		
		RoleResponseDto responseDto = new RoleResponseDto();
		Integer pageNumber = Integer.valueOf(pageNo);
		Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageSize));
		List<RoleInDto> roleDtoList = userServices.getAllRoles(pageable);
		responseDto.setRoleDtoList(roleDtoList);
		Long noOfTotalRole = roleRepository.count();
		if(noOfTotalRole>(pageNumber+1)*pageable.getPageSize())
			  responseDto.setLastPage(false);
		  else
			  responseDto.setLastPage(true);
		Long totalPages = noOfTotalRole/pageable.getPageSize();
		if(noOfTotalRole%pageable.getPageSize() != 0) {
			totalPages = totalPages+1;
		}
		responseDto.setTotalPages(totalPages);
		return new ResponseEntity<>(responseDto,HttpStatus.OK);
	}
	
/*	@ApiOperation(value = "To create new profile", response = RoleOutDto.class)
	@PostMapping(value = "/users/profile")
	public ResponseEntity<?> addRole(@Valid @RequestBody  RoleInDto roleIn){
		LOGGER.info("<-----------------------Start addRole() method in UserManagementController-------------------------------->");		
		LOGGER.info("Input details: " + roleIn.toString());
		Role role = new Role();
		role.setRoleName(roleIn.getRoleName());
		role.setDescription(roleIn.getDescription());
		RoleOutDto roleOut = new RoleOutDto();
		
		try {
			Role createdRole = userServices.addRole(role);
			
			roleOut.setSuccess(true);
			roleOut.setResponseCode(HttpStatus.OK.value());
			roleOut.setMessage("Profile created successfully!");
			
			return new ResponseEntity<>(roleOut, HttpStatus.OK);
			
		} catch (Exception e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500);
		}finally {
			LOGGER.info("<-----------------------End addRole() method in UserManagementController-------------------------------->");		
		}
		
	}
*/
	@ApiOperation(value = "To get all menu's list", response = MenuDto.class)
	@PutMapping(value = "/users/profile")
	public ResponseEntity<MenuDto> getAllMenu(@RequestBody  RoleInputDto roleInput){
		LOGGER.info("<-----------------------Start getAllMenuSubmenu() method in UserManagementController-------------------------------->");		
		try {
			MenuDto menuDto = userServices.getAllMenu(roleInput.getRoleName());
			
			return new ResponseEntity<>(menuDto, HttpStatus.OK);
			
		} catch (CustomUserMgmtException e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500, e.getMessage());
		}finally {
			LOGGER.info("<-----------------------End getAllMenuSubmenu() method in UserManagementController-------------------------------->");		
		}
		
	}

	@ApiOperation(value = "To create profile with Menu and subMenu mapping", response = RoleOutDto.class)
	@PostMapping(value = "/user/profile/mapping")
	public ResponseEntity<RoleOutDto> createRoleMenuMapping(@Valid @RequestBody  ProfileInDto profileInDto){
		LOGGER.info("<-----------------------Start createRoleMenuMapping() method in UserManagementController-------------------------------->");		
		LOGGER.info("Input details: " + profileInDto.toString());
		RoleOutDto roleOutDto = new RoleOutDto();
		
		try {
			userServices.createRoleMenuMapping(profileInDto);
			
			roleOutDto.setSuccess(true);
			roleOutDto.setResponseCode(HttpStatus.OK.value());
			roleOutDto.setMessage("Profile creation and mapping done successfully!");
			
			return new ResponseEntity<>(roleOutDto, HttpStatus.OK);
			
		} catch (CustomUserMgmtException e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500, e.getMessage());
		}finally {
			LOGGER.info("<-----------------------End createRoleMenuMapping() method in UserManagementController-------------------------------->");		
		}
		
	}
	
	@ApiOperation(value = "To delete the profile mapping with Menu and subMenu", response = RoleOutDto.class)
	@PutMapping(value = "/user/profile")
	public ResponseEntity<RoleOutDto> deleteRoleMenuMapping(@Valid @RequestBody  RoleInDto roleIn){
		LOGGER.info("<-----------------------Start deleteRoleMenuMapping() method in UserManagementController-------------------------------->");		
		LOGGER.info("Input details: " + roleIn.toString());
		RoleOutDto roleOut = new RoleOutDto();
		
		try {
			userServices.deleteRoleMenuMapping(roleIn);
			
			roleOut.setSuccess(true);
			roleOut.setResponseCode(HttpStatus.OK.value());
			roleOut.setMessage("Profile mapping deleted successfully!");
			
			return new ResponseEntity<>(roleOut, HttpStatus.OK);
			
		} catch (CustomUserMgmtException e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500, e.getMessage());
		}finally {
			LOGGER.info("<-----------------------End deleteRoleMenuMapping() method in UserManagementController-------------------------------->");		
		}
		
	}

	@ApiOperation(value = "To update profile mapping with Menu and subMenu", response = RoleOutDto.class)
	@PutMapping(value = "/user/profile/mapping")
	public ResponseEntity<RoleOutDto> updateRoleMenuMapping(@Valid @RequestBody  ProfileInDto profileInDto){
		LOGGER.info("<-----------------------Start updateRoleMenuMapping() method in UserManagementController-------------------------------->");		
		LOGGER.info("Input details: " + profileInDto.toString());
		RoleOutDto roleOutDto = new RoleOutDto();
		
		try {
			userServices.updateRoleMenuMapping(profileInDto);
			
			roleOutDto.setSuccess(true);
			roleOutDto.setResponseCode(HttpStatus.OK.value());
			roleOutDto.setMessage("Profile mapping updated successfully!");
			
			return new ResponseEntity<>(roleOutDto, HttpStatus.OK);
			
		} catch (CustomUserMgmtException e) {
			LOGGER.info("Error Description: " + e.getMessage());		
			throw new CustomUserMgmtException(500, e.getMessage());
		}finally {
			LOGGER.info("<-----------------------End updateRoleMenuMapping() method in UserManagementController-------------------------------->");		
		}
		
	}
	
}
