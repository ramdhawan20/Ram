package com.hcl.bss.services;

import com.hcl.bss.domain.Users;

import java.util.List;

public interface UserServices {
    Users findById(int id);
    List<Users> findByUserFirstName(String name);
}
