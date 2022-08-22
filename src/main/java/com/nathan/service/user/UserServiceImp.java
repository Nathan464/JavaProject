package com.nathan.service.user;

import com.nathan.pojo.User;

public interface UserServiceImp {
    public User login(String userCode, String password);
}
