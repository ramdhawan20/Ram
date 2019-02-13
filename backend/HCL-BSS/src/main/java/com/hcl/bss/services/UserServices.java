package com.hcl.bss.services;

import com.hcl.bss.domain.Users;

public interface UserServices {
    Users findById(int id);
}
