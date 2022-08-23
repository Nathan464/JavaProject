package com.nathan.dao.provider;

import com.nathan.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    int add(Connection connection, Provider provider) throws SQLException;

    List<Provider> getProviderList(Connection connection, String proName, String proCode,
                                          Integer currentPageNo, Integer pageSize) throws SQLException;

    int getProviderCount(Connection connection, String proName, String proCode) throws SQLException;

    int deleteProviderById(Connection connection, String id) throws SQLException;

    Provider getProviderById(Connection connection, String id) throws SQLException;

    int modify(Connection connection, Provider provider) throws SQLException;

}
