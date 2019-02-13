package com.hcl.bss.dao;

import com.hcl.bss.domain.Users;

public interface UserDAO {
    Users findById(int id);
}
