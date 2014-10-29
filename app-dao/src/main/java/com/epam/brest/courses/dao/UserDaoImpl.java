package com.epam.brest.courses.dao;
import com.epam.brest.courses.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by mentee-42 on 20.10.14.
 */
public class UserDaoImpl implements UserDao {
    public static final String ADD_NEW_USER_SQL = "insert into USER (userid, login, name) values (:userid, :login, :name)";
    public static final String DELETE_USER_SQL = "delete from USER where user_id = ?";
    public static final String UPDATE_USER_SQL = "update user set name = :name, login = :login where userid = :userid";
    public static final String SELECT_USER_BY_LOGIN_SQL = "select userid, login, name from USER where LCASE(login) = ?";
    public static final String SELECT_ALL_USERS_SQL = "select userid, login, name from USER";
    public static final String SELECT_USER_BY_ID_SQL = "select userid, login, name from USER where userid = ?";
    public static final String USER_ID = "userid";
    public static final String LOGIN = "login";
    public static final String NAME = "name";
    private static final Logger LOGGER = LogManager.getLogger();
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    @Override
    public void addUser(User user) {
        LOGGER.debug("addUser({}) ", user);
        Map<String, Object> parameters = new HashMap(3);
        parameters.put(NAME, user.getName());
        parameters.put(LOGIN, user.getLogin());
        parameters.put(USER_ID, user.getUserId());
        namedJdbcTemplate.update(ADD_NEW_USER_SQL, parameters);
    }

    @Override
    public User getUserByLogin (String login) {
        LOGGER.debug("getUserByLogin(login={})", login);
        return jdbcTemplate.queryForObject(SELECT_USER_BY_LOGIN_SQL,
                new String [] {login.toLowerCase()}, new UserMapper() );
    }

    @Override
    public List<User> getUsers() {
        LOGGER.debug("get users()");
        return jdbcTemplate.query(SELECT_ALL_USERS_SQL, new UserMapper());
    }
    @Override
    public void removeUser(Long userId) {
        LOGGER.debug("removeUser(userId={}) ", userId);
        jdbcTemplate.update(DELETE_USER_SQL, userId);
    }

    @Override
    public User getUserById(long userId) {
        LOGGER.debug("getUserById(userId={})", userId);
        return jdbcTemplate.queryForObject(SELECT_USER_BY_ID_SQL,
                new UserMapper(), userId);
    }
    @Override
    public void updateUser(User user) {
        LOGGER.debug("updateUser({}).. ", user);
        Map<String, Object> parameters = new HashMap(3);
        parameters.put(NAME, user.getName());
        parameters.put(LOGIN, user.getLogin());
        parameters.put(USER_ID, user.getUserId());
        namedJdbcTemplate.update(UPDATE_USER_SQL, parameters);
    }
    public class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong(USER_ID));
            user.setLogin(rs.getString(LOGIN));
            user.setName(rs.getString(NAME));
            return user;
        }
    }
}


/*package com.epam.brest.courses.domain;

public class User {
    private Long userId;
    private String login;
    private String name;

    public User() {
    }

    public User(Long userId, String login, String name) {
        this.userId = userId;
        this.login = login;
        this.name = name;
    }


    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (!userId.equals(other.getUserId()))
            return false;
        if (!getName().equals(other.getName()))
            return false;
        if (!getLogin().equals(other.getLogin()))
            return false;
        return true;
    }
}

*/