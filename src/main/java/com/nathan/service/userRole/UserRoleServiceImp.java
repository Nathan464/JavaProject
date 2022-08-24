package com.nathan.service.userRole;

import com.nathan.dao.BaseDao;
import com.nathan.dao.userRole.UserRoleDao;
import com.nathan.dao.userRole.UserRoleDaoImp;
import com.nathan.pojo.Role;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserRoleServiceImp implements UserRoleService {
    private final UserRoleDao userRoleDao;

    public UserRoleServiceImp() {
        this.userRoleDao = new UserRoleDaoImp();
    }

    @Override
    public List<Role> getRoleList() throws SQLException {
        List<Role> roleList = null;
        Connection connection = BaseDao.getConnection();
        if (connection != null) {
            try {
                roleList = userRoleDao.getRoleList(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(connection, null, null);
            }
        }
        return roleList;
    }
}
