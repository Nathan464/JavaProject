package com.nathan.service.user;

import com.nathan.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User login(String userCode, String passWord) throws SQLException;

    boolean modifyPassword(Connection connection,int id, String newPassword) throws SQLException;

    List<User> getUserList(String userName, int userRole, int currentPageNo,
                           int pageSize) throws SQLException;

    int getUserCount(String userName, int userRole) throws SQLException;

    boolean addUser(User user) throws SQLException;

    boolean isUserExist(String userCode) throws SQLException;

    boolean deleteUser(int id) throws SQLException;

    User viewUser(int id) throws SQLException;

    boolean modifyUser(User user) throws SQLException;
}
