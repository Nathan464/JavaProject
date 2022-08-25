package com.nathan.dao.user;

import com.mysql.cj.util.StringUtils;
import com.nathan.dao.BaseDao;
import com.nathan.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImp implements UserDao {
    @Override
    public User getLoginUser(String userCode, String password) throws SQLException {
        String sql = "select * from smbms_user where userCode=? and userPassword=?";
        Connection connection = BaseDao.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        Object[] params = {userCode, password};
        if (connection != null) {
            resultSet = BaseDao.execute(connection, preparedStatement, sql, params, resultSet);
            if (resultSet != null) {
                try {
                    if (resultSet.next()) {
                        user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setUserCode(resultSet.getString("userCode"));
                        user.setUserName(resultSet.getString("userName"));
                        user.setUserPassword(resultSet.getString("userPassword"));
                        user.setGender(resultSet.getInt("gender"));
                        user.setBirthday(resultSet.getDate("birthday"));
                        user.setPhone(resultSet.getString("phone"));
                        user.setAddress(resultSet.getString("address"));
                        user.setUserRole(resultSet.getInt("userRole"));
                        user.setCreatedBy(resultSet.getInt("createdBy"));
                        user.setCreationDate(resultSet.getTimestamp("creationDate"));
                        user.setModifyBy(resultSet.getInt("modifyBy"));
                        user.setModifyDate(resultSet.getTimestamp("modifyDate"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            BaseDao.closeResources(connection, preparedStatement, resultSet);
        }
        return user;
    }

    @Override
    public boolean modifyPassword(Connection connection, int id, String newPassword) throws SQLException {
        String sql = "update smbms_user set userPassword=? where id=?";
        boolean flag = false;
        PreparedStatement preparedStatement = null;
        Object[] params = {newPassword, id};
        if (connection != null) {
            try {
                BaseDao.execute(connection, preparedStatement, sql, params);
                flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(connection, preparedStatement, null);
            }
        }
        return flag;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole,
                                  int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> listUser = new ArrayList<>();
        if (connection != null) {
            StringBuilder stringBuilder = new StringBuilder();
            List<Object> list = new ArrayList<>();
            stringBuilder.append(
                    "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            if (!StringUtils.isNullOrEmpty(userName)) {
                stringBuilder.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                stringBuilder.append(" and u.userRole = ?");
                list.add(userRole);
            }
            stringBuilder.append(" order by creationDate DESC limit ?,?");  //按照创建时间降序
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            Object[] params = list.toArray();
            resultSet = BaseDao.execute(connection, preparedStatement, stringBuilder.toString(),
                    params, resultSet);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
                user.setAge(user.getAge());  //根据生日来计算的，并没有存放在数据库中
                listUser.add(user);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return listUser;
    }

    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        int count = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (connection != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                stringBuilder.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                stringBuilder.append(" and u.userRole = ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();
            resultSet = BaseDao.execute(connection, preparedStatement, stringBuilder.toString(),
                    params, resultSet);
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return count;
    }

    @Override
    public boolean addUser(Connection connection, User user) throws SQLException {
        boolean flag = false;
        int count;
        String sql = "insert into smbms_user(userCode,userName,userPassword,gender,birthday," +
                "phone,address,userRole,createdBy,creationDate,modifyBy," +
                "modifyDate) values(?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement preparedStatement = null;
        if (connection != null) {
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, user.getUserCode());
                preparedStatement.setObject(2, user.getUserName());
                preparedStatement.setObject(3, user.getUserPassword());
                preparedStatement.setObject(4, user.getGender());
                preparedStatement.setObject(5, user.getBirthday());
                preparedStatement.setObject(6, user.getPhone());
                preparedStatement.setObject(7, user.getAddress());
                preparedStatement.setObject(8, user.getUserRole());
                preparedStatement.setObject(9, user.getCreatedBy());
                preparedStatement.setObject(10, user.getCreationDate());
                preparedStatement.setObject(11, user.getModifyBy());
                preparedStatement.setObject(12, user.getModifyDate());
                count = preparedStatement.executeUpdate();
                if (count > 0) flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, null);
            }
        }
        return flag;
    }

    @Override
    public boolean isUserExist(String userCode) throws SQLException {
        boolean flag = false;
        String sql = "select * from smbms_user where userCode=?";
        Connection connection = BaseDao.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {userCode};
        if (connection != null) {
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, sql, params, resultSet);
                if (resultSet.next()) {
                    if (resultSet.getString("userCode").equals(userCode) && !userCode.equals("")) {
                        flag = true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(connection, preparedStatement, resultSet);
            }
        }
        return flag;
    }

    @Override
    public boolean deleterUser(Connection connection, int id) throws SQLException {
        String sql = "delete from smbms_user where id=?";
        boolean flag = false;
        PreparedStatement preparedStatement = null;
        Object[] params = {id};
        if (connection != null) {
            try {
                if (BaseDao.execute(connection, preparedStatement, sql, params) > 0) {
                    flag = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, null);
            }
        }
        return flag;
    }

    @Override
    public User viewUser(Connection connection, int id) throws SQLException {
        String sql = "select *,(select roleName as userRoleName from smbms_role " +
                "where id=(select userRole from smbms_user where id=?)) " +
                "as userRoleName from smbms_user where id=?;";
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {id, id};
        if (connection != null) {
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, sql, params, resultSet);
                if (resultSet.next()) {
                    user = new User();
                    user.setUserName(resultSet.getString("userName"));
                    user.setUserCode(resultSet.getString("userCode"));
                    user.setGender(resultSet.getInt("gender"));
                    user.setBirthday(resultSet.getDate("birthday"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("address"));
                    user.setUserRoleName(resultSet.getString("userRoleName"));
                    user.setUserRole(resultSet.getInt("userRole"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, resultSet);
            }
        }
        return user;
    }

    @Override
    public boolean modifyUser(Connection connection, User user) throws SQLException {
        int count;
        boolean flag = false;
        String sql = "update smbms_user set modifyBy=?,modifyDate=?,userName=?,gender=?," +
                "phone=?,address=?,userRole=? where id=?;";
        PreparedStatement preparedStatement = null;
        Object[] params = {user.getModifyBy(), user.getModifyDate(), user.getUserName(), user.getGender(),
                user.getPhone(), user.getAddress(), user.getUserRole(), user.getId()};
        if (connection != null) {
            try {
                count = BaseDao.execute(connection, preparedStatement, sql, params);
                if (count > 0) flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, null);
            }
        }
        return flag;
    }
}
