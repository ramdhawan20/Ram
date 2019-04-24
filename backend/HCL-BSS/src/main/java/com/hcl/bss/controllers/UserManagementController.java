package com.hcl.bss.controllers;

import static com.hcl.bss.constants.ApplicationConstants.ADMIN;
import static com.hcl.bss.constants.ApplicationConstants.NORMAL;
import static com.hcl.bss.constants.ApplicationConstants.ROLE_ADMIN;
import static com.hcl.bss.constants.ApplicationConstants.ROLE_NORMAL;
import static com.hcl.bss.constants.ApplicationConstants.ACTIVE;
import static com.hcl.bss.constants.ApplicationConstants.INACTIVE;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.User;
import com.hcl.bss.dto.DropDownOutDto;
import com.hcl.bss.dto.UserDto;
import com.hcl.bss.dto.UserInDto;
import com.hcl.bss.dto.UserOutDto;
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
	@PutMapping(value = "/userm/users")
	public ResponseEntity<?> findAllUser(@Valid @RequestBody UserInDto userIn) {
	//public ResponseEntity<?> findAllUser() {
		LOGGER.info("<-----------------------Start findAllUser() method-------------------------------->");
		LOGGER.info("I/P details: " + userIn.toString());
		UserDto userDto = null;
		List<UserDto> userListOut = new ArrayList<>();
		UserOutDto userOut = new UserOutDto();
		List<User> userList = null;
		//Pageable pageable = new PageRequest(userIn.getPageNo(), 1);
		Pageable pageable = new PageRequest(userIn.getPageNo(), Integer.parseInt(pageSize));
		
		try {
			userList = userServices.findAllUser(userIn, pageable);
			
			if(userList == null && userList.size() == 0) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.NOT_FOUND.value());
				userOut.setMessage("No user found!");
				userOut.setUserList(null);

				return new ResponseEntity<>(userOut, HttpStatus.NOT_FOUND);
			}
			
			for(User usr : userList) {
				userDto = new UserDto();
				//userDto.setUserProfile(usr.getRoleId()== ROLE_ADMIN ? ADMIN : NORMAL);
				userDto.setUserId(usr.getUserId());
				userDto.setUserFirstName(usr.getUserFirstName());
				userDto.setUserMiddleName(usr.getUserMiddleName()==null ? "" : usr.getUserMiddleName());
				userDto.setUserLastName(usr.getUserLastName()==null ? "" : usr.getUserLastName());
				userDto.setStatus(usr.getIsLocked()==0 ? ACTIVE : INACTIVE);
				
				userListOut.add(userDto);
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
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End findAllUser() method-------------------------------->");		
		}
		
	}
	
	@ApiOperation(value = "To create new user", response = UserOutDto.class)
	@PostMapping(value = "/userm/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody  UserInDto userIn) {
		LOGGER.info("<-----------------------Start addUser() method-------------------------------->");		
		LOGGER.info("Input details: " + userIn.toString());
		
		User user = new User();
		User createdUser = null;
		UserOutDto userOut = new UserOutDto();
		
		try {
			user.setUserId(userIn.getUserId());
			if(userIn.getUserFirstName() == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("UserFirstName can not be null!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);				
			}
			
			if("".equalsIgnoreCase(userIn.getUserFirstName().trim())) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("UserFirstName can not be blank!");
	
				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
			user.setUserFirstName(userIn.getUserFirstName());
			user.setUserMiddleName(userIn.getUserMiddleName());
			user.setUserLastName(userIn.getUserLastName());
			
			if(userIn.getUserProfile() == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User Profile can not be null!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
			if("".equalsIgnoreCase(userIn.getUserProfile().trim())) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User Profile can not be blank!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
			//user.setRoleId(userIn.getUserProfile().trim().equalsIgnoreCase(ADMIN) ? ROLE_ADMIN : ROLE_NORMAL);
			user.setPassword(userIn.getAttribute());
			
			createdUser = this.userServices.addUser(user);
			
			if(createdUser == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User not created!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
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
			LOGGER.info("<-----------------------Start finally block-------------------------------->");
			user = null;
			createdUser = null;
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End addUser() method-------------------------------->");		
		}
	}

	@ApiOperation(value = "To update existing user", response = UserOutDto.class)
	@PutMapping(value = "/userm/user")
	public ResponseEntity<?> editUser(@Valid @RequestBody  UserInDto userIn) {
		LOGGER.info("<-----------------------Start editUser() method-------------------------------->");		
		LOGGER.info("Input details: " + userIn.toString());

		User user = new User();
		UserOutDto userOut = new UserOutDto();
		User updatedUser = null;
		
		try {
			user.setUserId(userIn.getUserId());
			user.setUserFirstName(userIn.getUserFirstName());
			user.setUserMiddleName(userIn.getUserMiddleName());
			user.setUserLastName(userIn.getUserLastName());
			if(userIn.getUserProfile() == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User Profile can not be null!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
			if("".equalsIgnoreCase(userIn.getUserProfile().trim())) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User Profile can not be blank!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
			//user.setRoleId(userIn.getUserProfile().trim().equalsIgnoreCase(ADMIN) ? ROLE_ADMIN : ROLE_NORMAL);

			updatedUser = this.userServices.editUser(user);

			if(updatedUser == null) {
				userOut.setSuccess(false);
				userOut.setResponseCode(HttpStatus.BAD_REQUEST.value());
				userOut.setMessage("User not updated!");

				return new ResponseEntity<>(userOut, HttpStatus.BAD_REQUEST);
			}
			
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
			LOGGER.info("<-----------------------Start finally block-------------------------------->");
			user = null;
			updatedUser = null;
			LOGGER.info("<-----------------------End finally block-------------------------------->");
			LOGGER.info("<-----------------------End editUser() method-------------------------------->");		
		}
		
	}

	@ApiOperation(value = "To activate/deactivate existing user", response = UserOutDto.class)
	@PutMapping(value = "/userm/activate")
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
	@PutMapping(value = "/userm/reset")
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
	@GetMapping(value = "/userm/getDropDownList")
	public ResponseEntity<?> getDropDownList(@RequestParam(value = "dropDownCode", required=true) String dropDownCode) {
		LOGGER.info("<-----------------------Start getDropDownList() method in UserManagementController-------------------------------->");		
		List<String> dropDownList = null;
		DropDownOutDto dropDownOut = new DropDownOutDto();
		
		try {
			dropDownList = userServices.getDropDownList(dropDownCode);
			
			if (dropDownList == null) {
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


}
