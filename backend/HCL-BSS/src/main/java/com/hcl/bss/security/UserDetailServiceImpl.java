package com.hcl.bss.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.hcl.bss.domain.Role;
import com.hcl.bss.domain.User;
import com.hcl.bss.repository.UserRepository;

@Component
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(username);
		
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), getAuthority(user));
	}

	private List<GrantedAuthority> getAuthority(User user) {
		List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : user.getRoleList()) {
        	
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(grantedAuthority);
        }
        System.out.println("user authorities are " + authorities.toString());
        return authorities;
		//return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	/*public List<User> getUsers() {
		return userRepository.findAll();
	}*/

}
