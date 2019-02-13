package com.hcl.bss.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hcl.bss.domain.Users;
import com.hcl.bss.dao.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hcl.bss.dto.UserDetails;

import javax.persistence.TypedQuery;


@Repository
public class UserRepository implements UserDAO {

    private SessionFactory sessionFactory;
    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Users findById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        TypedQuery<Users> query = session.getNamedQuery("findUserById");
        query.setParameter("id", id);
        Users user = query.getSingleResult();
        return user;
    }



    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL = "select * from user";

    public List<UserDetails> isData() {

        List<UserDetails> users = new ArrayList<UserDetails>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);

        for (Map<String, Object> row : rows)
        {
            UserDetails userDetails = new UserDetails();
            userDetails.setUserId((String)row.get("user_id"));
            userDetails.setUserFirstName((String)row.get("user_fn"));
            userDetails.setUserLastName((String)row.get("user_ln"));

            users.add(userDetails);
        }

        return users;
    }
}