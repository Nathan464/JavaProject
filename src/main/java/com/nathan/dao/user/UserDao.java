package com.nathan.dao.user;

import com.nathan.dao.BaseDao;
import com.nathan.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao implements UserImp {
    @Override
    public User getUser(String userCode, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            ResultSet execute = BaseDao.execute(
                    connection, sql, params, resultSet, preparedStatement);
            if (execute.next()) {
                user = new User();
                user.setId(execute.getInt("Id"));
                user.setUserCode(execute.getString("userCode"));
                user.setUserName(execute.getString("userName"));
                user.setUserPassword(execute.getString("userPassword"));
                user.setGender(execute.getInt("gender"));
                user.setBirthday(execute.getDate("birthday"));
                user.setPhone(execute.getString("phone"));
                user.setAddress(execute.getString("address"));
                user.setUserRole(execute.getInt("userRole"));
                user.setCreatedBy(execute.getInt("createdBy"));
                user.setCreationDate(execute.getTimestamp("creationDate"));
                user.setModifyBy(execute.getInt("modifyBy"));
                user.setModifyDate(execute.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, preparedStatement, execute);
        }
        return user;
    }
}
