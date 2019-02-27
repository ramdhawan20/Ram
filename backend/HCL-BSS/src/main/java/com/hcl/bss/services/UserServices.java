package com.hcl.bss.services;

import com.hcl.bss.domain.User;

import java.util.List;

public interface UserServices {
    User findById(int id);
    List<User> findByUserFirstName(String name);
}
