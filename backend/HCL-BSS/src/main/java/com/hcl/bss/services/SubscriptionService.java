package com.hcl.bss.services;

import java.util.List;

import com.hcl.bss.domain.Subscription1;
import com.hcl.bss.dto.SubscriptionInOut;

public interface SubscriptionService {

/*	User findById(String userId);
	
    List<User> findByUserFirstName(String name);
*/    
    //Iterable<Subscription> findAllSubscription();
    List<Subscription1> findAllSubscription(SubscriptionInOut subscriptionIn);
    //List<Subscription> findAllSubscription(String subscriptionIn);
    
/*    User addUser(User user);
    
    User editUser(User user);
    
    User activateUser(User user);
    
    User resetUser(User user);
*/}
