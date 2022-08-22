package com.nathan.dao.user;

import com.nathan.pojo.User;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserImp{
    public User getUser(String userCode, Connection connection) throws SQLException;
}
