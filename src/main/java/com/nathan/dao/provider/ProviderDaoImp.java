package com.nathan.dao.provider;

import com.mysql.cj.util.StringUtils;
import com.nathan.dao.BaseDao;
import com.nathan.pojo.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImp implements ProviderDao {
    @Override
    public int add(Connection connection, Provider provider) throws SQLException {
        int count = 0;
        PreparedStatement preparedStatement = null;
        if (connection != null) {
            String sql = "insert into smbms_provider (proCode,proName,proDesc," +
                    "proContact,proPhone,proAddress,proFax,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?)";
            Object[] params = {provider.getProCode(), provider.getProName(), provider.getProDesc(),
                    provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getCreatedBy(), provider.getCreationDate()};
            count = BaseDao.execute(connection, preparedStatement, sql, params);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        return count;
    }

    @Override
    public List<Provider> getProviderList(Connection connection, String proName, String proCode,
                                          Integer currentPageNo, Integer pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Provider> providerList = new ArrayList<>();
        if (connection != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select * from smbms_provider where 1=1 ");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(proName)) {
                stringBuilder.append(" and proName like ?");
                list.add("%" + proName + "%");
            }
            if (!StringUtils.isNullOrEmpty(proCode)) {
                stringBuilder.append(" and proCode like ?");
                list.add("%" + proCode + "%");
            }
            if (currentPageNo != null && pageSize != null) {
                stringBuilder.append(" order by creationDate DESC limit ?,?");
                currentPageNo = (currentPageNo - 1) * pageSize;
                list.add(currentPageNo);
                list.add(pageSize);
            }
            Object[] params = list.toArray();
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, stringBuilder.toString(),
                        params, resultSet);
                while (resultSet.next()) {
                    Provider provider = new Provider();
                    provider.setId(resultSet.getInt("id"));
                    provider.setProCode(resultSet.getString("proCode"));
                    provider.setProName(resultSet.getString("proName"));
                    provider.setProDesc(resultSet.getString("proDesc"));
                    provider.setProContact(resultSet.getString("proContact"));
                    provider.setProPhone(resultSet.getString("proPhone"));
                    provider.setProAddress(resultSet.getString("proAddress"));
                    provider.setProFax(resultSet.getString("proFax"));
                    provider.setCreationDate(resultSet.getTimestamp("creationDate"));
                    providerList.add(provider);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, resultSet);
            }
        }
        return providerList;
    }

    @Override
    public int getProviderCount(Connection connection, String proName, String proCode) throws SQLException {
        int count = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (connection != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select count(1) as count from smbms_provider where 1=1");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(proName)) {
                stringBuilder.append(" and proName like ?");
                list.add("%" + proName + "%");
            }
            if (!StringUtils.isNullOrEmpty(proCode)) {
                stringBuilder.append(" and proCode like ?");
                list.add("%" + proCode + "%");
            }
            Object[] params = list.toArray();
            try {
                resultSet = BaseDao.execute(connection, preparedStatement,
                        stringBuilder.toString(), params, resultSet);
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, resultSet);
            }
        }
        return count;
    }

    @Override
    public int deleteProviderById(Connection connection, String id) throws SQLException {
        PreparedStatement preparedStatement = null;
        int count = 0;
        if (connection != null) {
            String sql = "delete from smbms_provider where id=?";
            Object[] params = {id};
            count = BaseDao.execute(connection, preparedStatement, sql, params);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        return count;
    }

    @Override
    public Provider getProviderById(Connection connection, String id) throws SQLException {
        PreparedStatement preparedStatement = null;
        Provider provider = null;
        ResultSet resultSet = null;
        if (connection != null) {
            String sql = "select * from smbms_provider where id=?";
            Object[] params = {id};
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, sql, params, resultSet);
                if (resultSet.next()) {
                    provider = new Provider();
                    provider.setId(resultSet.getInt("id"));
                    provider.setProCode(resultSet.getString("proCode"));
                    provider.setProName(resultSet.getString("proName"));
                    provider.setProDesc(resultSet.getString("proDesc"));
                    provider.setProContact(resultSet.getString("proContact"));
                    provider.setProPhone(resultSet.getString("proPhone"));
                    provider.setProAddress(resultSet.getString("proAddress"));
                    provider.setProFax(resultSet.getString("proFax"));
                    provider.setCreatedBy(resultSet.getInt("createdBy"));
                    provider.setCreationDate(resultSet.getTimestamp("creationDate"));
                    provider.setModifyBy(resultSet.getInt("modifyBy"));
                    provider.setModifyDate(resultSet.getTimestamp("modifyDate"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, preparedStatement, resultSet);
            }

        }
        return provider;
    }

    @Override
    public int modify(Connection connection, Provider provider) throws SQLException {
        int count = 0;
        PreparedStatement preparedStatement = null;
        if (connection != null) {
            String sql = "update smbms_provider set proName=?,proDesc=?,proContact=?," +
                    "proPhone=?,proAddress=?,proFax=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {provider.getProName(), provider.getProDesc(), provider.getProContact(),
                    provider.getProPhone(), provider.getProAddress(), provider.getProFax(),
                    provider.getModifyBy(), provider.getModifyDate(), provider.getId()};
            count = BaseDao.execute(connection,preparedStatement,sql,params);
            BaseDao.closeResources(null,preparedStatement,null);
        }
        return count;
    }
}
