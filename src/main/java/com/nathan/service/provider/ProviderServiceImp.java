package com.nathan.service.provider;

import com.nathan.dao.BaseDao;
import com.nathan.dao.bill.BillDao;
import com.nathan.dao.bill.BillDaoImp;
import com.nathan.dao.provider.ProviderDao;
import com.nathan.dao.provider.ProviderDaoImp;
import com.nathan.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderServiceImp implements ProviderService {
    private final ProviderDao providerDao;
    private final BillDao billDao;

    public ProviderServiceImp() {
        providerDao = new ProviderDaoImp();
        billDao = new BillDaoImp();
    }

    @Override
    public boolean add(Provider provider) throws SQLException {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (providerDao.add(connection, provider) > 0) {
                flag = true;
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode, Integer currentPageNo,
                                          Integer pageSize) throws SQLException {
        List<Provider> providerList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection, proName, proCode, currentPageNo, pageSize);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return providerList;
    }

    @Override
    public int getProviderCount(String proName, String proCode) throws SQLException {
        int count = 0;
        Connection connection = BaseDao.getConnection();
        if (connection != null) {
            try {
                count = providerDao.getProviderCount(connection, proName, proCode);
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                BaseDao.closeResources(connection, null, null);
            }
        }
        return count;
    }

    @Override
    public int deleteProviderById(String providerId) throws SQLException {
        Connection connection = null;
        int billCount;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            billCount = billDao.getBillCountByProviderId(connection, providerId);
            if (billCount == 0) {
                providerDao.deleteProviderById(connection, providerId);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return billCount;
    }

    @Override
    public Provider getProviderById(String providerId) throws SQLException {
        Provider provider = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection, providerId);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return provider;
    }

    @Override
    public boolean modify(Provider provider) throws SQLException {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            if (providerDao.modify(connection, provider) > 0) {
                flag = true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }
}
