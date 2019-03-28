package com.hcl.bss.repository.specification;
import org.springframework.data.jpa.domain.Specification;

import com.hcl.bss.domain.User;
/**
 * This is UserManagementSpecification is used to apply filter criteria on UserManagement
 *
 * @author- Vinay Panwar
 */
public class UserManagementSpecification {
	
	private UserManagementSpecification() {}

//root-->Entity(i.e. Subscription) cq --> CriteriaQuery , cb--> CriteriaBuilder

	public static Specification<User> hasUserId(String userId){
		return (root, cq, cb)-> cb.equal(root.get("userId"), userId);
	}

	public static Specification<User> isLocked(int isLocked){
		return (root, cq, cb)-> cb.equal(root.get("isLocked"), isLocked);
	}

	public static Specification<User> hasRoleId(int roleId){
		return (root, cq, cb)-> cb.equal(root.get("roleId"), roleId);
	}
	 
	public static Specification<User> hasUserName(String searchTerm) {
		return (root, query, cb) -> {
			String containsLikePattern = getContainsLikePattern(searchTerm);
			return cb.like(cb.lower(root.get("userFirstName")), containsLikePattern);
		};
	}

	private static String getContainsLikePattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		} else {
			return "%" + searchTerm.toLowerCase() + "%";
		}
	}
		
}