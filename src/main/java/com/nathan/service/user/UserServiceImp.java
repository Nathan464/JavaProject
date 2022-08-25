package com.nathan.service.user;

import com.nathan.dao.BaseDao;
import com.nathan.dao.user.UserDao;
import com.nathan.dao.user.UserDaoImp;
import com.nathan.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImp implements UserService{
    private final UserDao userDao;
    public UserServiceImp() {
        userDao = new UserDaoImp();
    }

    @Override
    public User login(String userCode, String password) throws SQLException {
        return userDao.getLoginUser(userCode,password);
    }

    @Override
    public boolean modifyPassword(Connection connection, int id, String newPassword) throws SQLException {
        return userDao.modifyPassword(connection,id,newPassword);
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo,
                                  int pageSize) throws SQLException {
        List<User> userList = null;
        Connection connection = BaseDao.getConnection();
        if (connection!=null){
            try {
                userList = userDao.getUserList(connection,userName,userRole,currentPageNo,pageSize);
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return userList;
    }

    @Override
    public int getUserCount(String userName, int userRole) throws SQLException {
        int count = 0;
        Connection connection = BaseDao.getConnection();
        if (connection!=null){
            try {
                count = userDao.getUserCount(connection,userName,userRole);
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return count;
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        boolean flag = false;
        Connection connection = BaseDao.getConnection();
        if (connection!=null){
            try {
                connection.setAutoCommit(false);
                flag = userDao.addUser(connection,user);
                connection.commit();
            }catch (SQLException exception){
                try {
                    connection.rollback();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                exception.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public boolean isUserExist(String userCode) throws SQLException {
        return userDao.isUserExist(userCode);
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean flag = true;
        Connection connection = BaseDao.getConnection();
        if (connection!=null){
            try {
                connection.setAutoCommit(false);
                flag = userDao.deleterUser(connection,id);
                connection.commit();
            }catch (SQLException exception){
                try {
                    connection.rollback();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                flag = false;
                exception.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public User viewUser(int id) throws SQLException {
        User user = null;
        Connection connection = BaseDao.getConnection();
        if (connection!=null){
            try {
                connection.setAutoCommit(false);
                user = userDao.viewUser(connection,id);
                connection.commit();
            }catch (SQLException e){
                try {
                    connection.rollback();
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return user;
    }

    @Override
    public boolean modifyUser(User user) throws SQLException {
        boolean flag = false;
        Connection connection = BaseDao.getConnection();
        if (connection!=null){
            try{
                connection.setAutoCommit(false);
                flag = userDao.modifyUser(connection,user);
                connection.commit();
            }catch (SQLException exception){
                flag = false;
                try {
                    connection.rollback();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                exception.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }
}
