package com.hcl.bss.services;

import com.hcl.bss.domain.Users;
import com.hcl.bss.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServicesImpl implements UserServices {
    private UserDAO userDAO;
    @Autowired
    public UserServicesImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public Users findById(int id) {
        return this.userDAO.findById(id);
    }
}