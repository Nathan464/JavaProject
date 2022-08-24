package com.nathan.service.provider;

import com.nathan.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface ProviderService {
    boolean add(Provider provider) throws SQLException;

    List<Provider> getProviderList(String proName, String proCode, Integer currentPageNo,
                                   Integer pageSize) throws SQLException;

    int getProviderCount(String proName, String proCode) throws SQLException;

    int deleteProviderById(String providerId) throws SQLException;

    Provider getProviderById(String providerId) throws SQLException;

    boolean modify(Provider provider) throws SQLException;
}
