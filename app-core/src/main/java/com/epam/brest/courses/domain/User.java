package com.epam.brest.courses.domain;


public interface User {
    public Long getUserId();
    public void setUserId(Long userId);
    public String getLogin();
    public void setLogin(String login);
    public String getName();
    public void setName(String name);
}
