package com.hcl.bss.repository;

import com.hcl.bss.domain.RatePlanVolume;
import com.hcl.bss.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * @author- Aditya gupta
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value= "SELECT * from tb_user_details where user_id=?1 and password=?2",nativeQuery = true)
    public User isUserExists(@Param("userId") String userId, @Param("userId") String password);
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