package com.epam.brest.courses.dao;
import com.epam.brest.courses.domain.User;
import java.util.List;
/**
 * Created by mentee-42 on 20.10.14.
 */
public interface UserDao {
    public void addUser(User user);
    public List<User> getUsers();
    public void removeUser(Long userId);
    public User getUserByLogin(String login);
    public User getUserById(long userId);
    public void updateUser(User user);
}

 /*@Override
    public void addUser(User user) {
        LOGGER.debug("addUser({}) ", user);
        Map<String, Object> parameters = new HashMap(3);
        parameters.put(NAME, user.getName());
        parameters.put(LOGIN, user.getLogin());
        parameters.put(USER_ID, user.getUserId());
        namedJdbcTemplate.update(ADD_NEW_USER_SQL, parameters);
    }*/