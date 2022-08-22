package com.nathan.service.user;

import com.nathan.dao.BaseDao;
import com.nathan.dao.user.UserDao;
import com.nathan.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService implements UserServiceImp{
    private UserDao userDao;
    public UserService(){userDao = new UserDao();}
    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getUser(userCode,connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        if (user!=null){
            if (!user.getUserPassword().equals(password)){
                user = null;
            }
        }
        return user;
    }
}
