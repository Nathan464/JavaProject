package com.nathan.dao.user;

import com.nathan.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    User getLoginUser(String userCode, String password) throws SQLException;

    boolean modifyPassword(int id, String newPassword) throws SQLException;

    List<User> getUserList(Connection connection, String userName, int userRole,
                           int currentPageNo, int pageSize) throws SQLException;

    int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    boolean addUser(Connection connection, User user) throws SQLException;

    boolean isUserExist(String userCode) throws SQLException;

    boolean deleterUser(Connection connection, int id) throws SQLException;

    User viewUser(Connection connection, int id) throws SQLException;

    boolean modifyUser(Connection connection, User user) throws SQLException;
}
