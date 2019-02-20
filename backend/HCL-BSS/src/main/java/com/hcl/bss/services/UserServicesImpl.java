package com.hcl.bss.services;

import com.hcl.bss.domain.Users;
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
    public Users findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<Users> findByUserFirstName(String name) {
        return userRepository.findByUserFirstName(name);
    }
}