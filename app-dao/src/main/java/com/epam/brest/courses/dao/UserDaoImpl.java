package com.epam.brest.courses.dao;
import com.epam.brest.courses.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {

    @Value("#{T(org.apache.commons.io.FileUtils).readFileToString((new org.springframework.core.io.ClassPathResource('${insert_into_user_path}')).file)}")
    public String addNewUserSql;

    @Value("#{T(org.apache.commons.io.FileUtils).readFileToString((new org.springframework.core.io.ClassPathResource('${remove_user}')).file)}")
    public String removeUserSql;

    public static final String UPDATE_USER_SQL = "update user set name = :name, login = :login where userid = :userid";
    public static final String SELECT_USER_BY_LOGIN_SQL = "select userid, login, name from USER where LCASE(login) = ?";
    public static final String SELECT_ALL_USERS_SQL = "select userid, login, name from USER";
    public static final String SELECT_USER_BY_ID_SQL = "select userId, login, name from USER where userid = ?";
    public static final String USER_ID = "userId";
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
        Assert.notNull(user);
        //Assert.isNull(user.getUserId());
        Assert.notNull(user.getLogin(), "User login should be specified.");
        Assert.notNull(user.getName(), "User name should be specified.");
        Map<String, Object> parameters = new HashMap(3);
        parameters.put(NAME, user.getName());
        parameters.put(LOGIN, user.getLogin());
        parameters.put(USER_ID, user.getUserId());
        namedJdbcTemplate.update(addNewUserSql, parameters);
    }
    @Override
    public List<User> getUsers() {
        LOGGER.debug("get users()");
        return jdbcTemplate.query(SELECT_ALL_USERS_SQL, new UserMapper());
    }
    @Override
    public void removeUserById(Long userId) {
        LOGGER.debug("removeUser(userId={}) ", userId);
        Map<String, Object> args = new HashMap(1);
        args.put(USER_ID, userId);
        namedJdbcTemplate.update(removeUserSql, args);
    }
    @Override
    public User getUserByLogin(String login) {
        LOGGER.debug("getUserByLogin(login={})", login);
        return jdbcTemplate.queryForObject(SELECT_USER_BY_LOGIN_SQL,
                new String[]{login.toLowerCase()}, new UserMapper());
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


    public boolean equals (User user1, User user2) {

        if (user1 == null && user2 == null)
            return false;

        if (user1.getClass() != user2.getClass()) {
            return false;
        }

        if (user1.getUserId()==user2.getUserId() && user1.getLogin()==user2.getLogin()
                && user1.getName()==user2.getName()) {
            return true;
        }
        return false;
    }


}