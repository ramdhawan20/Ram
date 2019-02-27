package com.hcl.bss.repository;

import com.hcl.bss.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author- Aditya gupta
 */
public interface UserRepository extends JpaRepository<User, Long> {

    public User findById(int id) ;

    public List<User> findByUserFirstName(String firstName);

/*    private SessionFactory sessionFactory;
    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }*/




/*    @Autowired
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
    }*/
}