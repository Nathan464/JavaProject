package com.nathan.service.userRole;

import com.nathan.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface UserRoleService {
    List<Role> getRoleList() throws SQLException;
}
