package com.nathan.dao.userRole;

import com.nathan.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserRoleDao {
    List<Role> getRoleList(Connection connection) throws SQLException;
}
