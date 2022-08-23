package com.nathan.dao.userRole;

import com.nathan.dao.BaseDao;
import com.nathan.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDaoImp implements UserRoleDao{
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        List<Role> roleList = new ArrayList<>();
        String sql = "select * from smbms_role";
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;
        Role role;
        if (connection!=null){
            preparedStatement = connection.prepareStatement(sql);
            try {
                resultSet = BaseDao.execute(connection,preparedStatement,sql,null,resultSet);
                while (resultSet.next()){
                    role = new Role();
                    role.setId(resultSet.getInt("id"));
                    role.setCreatedBy(resultSet.getInt("createdBy"));
                    role.setCreationDate(resultSet.getTimestamp("creationDate"));
                    role.setModifyBy(resultSet.getInt("modifyBy"));
                    role.setModifyDate(resultSet.getTimestamp("modifyDate"));
                    role.setRoleCode(resultSet.getString("roleCode"));
                    role.setRoleName(resultSet.getString("roleName"));
                    roleList.add(role);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(null,preparedStatement,resultSet);
            }
        }
        return roleList;
    }
}
