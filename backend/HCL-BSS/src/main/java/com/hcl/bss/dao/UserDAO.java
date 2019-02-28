package com.hcl.bss.dao;

import com.hcl.bss.domain.User;

public interface UserDAO {
    User findById(int id);
}
