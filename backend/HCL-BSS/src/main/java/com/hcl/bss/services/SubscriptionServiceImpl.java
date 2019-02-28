package com.hcl.bss.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bss.domain.Subscription1;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.repository.SubscriptionRepository1;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionRepository1 subscriptionRepository1;

 /*   @Override
    public User findById(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<User> findByUserFirstName(String name) {
        return userRepository.findByUserFirstName(name);
    }
*/
	@Override
	//public Iterable<Subscription> findAllSubscription() {
	//public List<Subscription> findAllSubscription(String subscriptionIn) {
	public List<Subscription1> findAllSubscription(SubscriptionInOut subscriptionIn) {
		return subscriptionRepository1.findAll(subscriptionIn.getSubscriptionId(), subscriptionIn.getCustomerName(), subscriptionIn.getEmail());
		//return subscriptionRepository.findAll(subscriptionIn);
	}
    
/*	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public User editUser(User user) {
		//fetch the user which you want to update
		User fetchUser = userRepository.findByUserId(user.getUserId());
		
		if(user.getUserFirstName() != null && !"".equalsIgnoreCase(user.getUserFirstName())) {
			fetchUser.setUserFirstName(user.getUserFirstName());
		}
		if(user.getUserMiddleName() != null && !"".equalsIgnoreCase(user.getUserMiddleName())) {
			fetchUser.setUserMiddleName(user.getUserMiddleName());
		}
		if(user.getUserLastName() != null && !"".equalsIgnoreCase(user.getUserLastName())) {
			fetchUser.setUserLastName(user.getUserLastName());
		}
		if(user.getRoleId() != -1) {
			fetchUser.setRoleId(user.getRoleId());
		}
		
		return this.userRepository.save(fetchUser);
	}
	
	Method used to activate and deactivate user
	@Override
	public User activateUser(User user) {
		//fetch the user which you want to update
		User fetchUser = userRepository.findByUserId(user.getUserId());
		
		if(user.getIsLocked() != -1) {
			fetchUser.setIsLocked(user.getIsLocked());
		}
		
		if(user.getIsLocked() == 0) {
			fetchUser.setIsLocked(1);
		}else {
			fetchUser.setIsLocked(1);
		}
		
		return this.userRepository.save(fetchUser);
	}
	
	@Override
	public User resetUser(User user) {
		//fetch the user which you want to update
		User fetchUser = userRepository.findByUserId(user.getUserId());
		
		if(user.getPassword() != null && !"".equalsIgnoreCase(user.getPassword())) {
			fetchUser.setPassword(user.getPassword());
		}
		
		return this.userRepository.save(fetchUser);
	}
*/    
}