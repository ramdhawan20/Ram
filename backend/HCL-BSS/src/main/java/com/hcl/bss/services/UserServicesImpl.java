package com.hcl.bss.services;

import com.hcl.bss.domain.User;
import com.hcl.bss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findByUserFirstName(String name) {
        return userRepository.findByUserFirstName(name);
    }
}