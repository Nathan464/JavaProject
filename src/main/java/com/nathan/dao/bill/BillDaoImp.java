package com.nathan.dao.bill;

import com.nathan.dao.BaseDao;
import com.nathan.pojo.Bill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImp implements BillDao {

    @Override
    public int getCountByCondition(String queryProductionName, Integer isPayment,
                                   Integer providerId) throws SQLException {
        String sql = "select count(1) as count from smbms_bill where 1=1";
        StringBuilder stringBuffer = new StringBuilder(sql);
        List<Object> list = new ArrayList<>();
        if (providerId != null) {
            stringBuffer.append(" and providerId=?");
            list.add(providerId);
        }
        if (isPayment != null) {
            stringBuffer.append(" and isPayment=?");
            list.add(isPayment);
        }
        if (queryProductionName != null && !queryProductionName.equals("")) {
            stringBuffer.append(" and productName like ?");
            list.add("%" + queryProductionName + "%");
        }
        Object[] params = list.toArray();
        Connection connection = BaseDao.getConnection();
        ResultSet resultSet = null;
        int count = 0;
        if (connection != null) {
            try {
                resultSet = BaseDao.execute(connection, null,
                        stringBuffer.toString(), params, null);
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(connection, null, resultSet);
            }
        }
        return count;
    }

    @Override
    public List<Bill> getBillListByCondition(String queryProductionName, Integer isPayment, Integer providerId,
                                             Integer currentPageNo, Integer pageSize) throws SQLException {
        List<Bill> billList = new ArrayList<>();
        String sql = "select * from smbms_bill where 1=1";
        List<Object> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(sql);
        if (queryProductionName != null && !queryProductionName.equals("")) {
            stringBuilder.append(" and productName like ?");
            list.add("%" + queryProductionName + "%");
        }
        if (isPayment != null) {
            stringBuilder.append(" and isPayment=?");
            list.add(isPayment);
        }
        if (providerId != null) {
            stringBuilder.append(" and providerId=?");
            list.add(providerId);
        }
        if (currentPageNo != null && pageSize != null) {
            stringBuilder.append(" order by creationDate desc limit ?,?");
            list.add((currentPageNo - 1) * pageSize);
            list.add(pageSize);
        }
        Object[] params = list.toArray();
        Connection connection = BaseDao.getConnection();
        ResultSet resultSet = null;
        if (connection != null) {
            try {
                resultSet = BaseDao.execute(connection, null,
                        stringBuilder.toString(), params, null);
                while (resultSet.next()) {
                    Bill bill = new Bill();
                    bill.setBillCode(resultSet.getString("billCode"));
                    bill.setCreationDate(resultSet.getTimestamp("creationDate"));
                    bill.setId(resultSet.getInt("id"));
                    bill.setProductName(resultSet.getString("productName"));
                    bill.setProviderId(resultSet.getInt("providerId"));
                    bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                    bill.setIsPayment(resultSet.getInt("isPayment"));
                    billList.add(bill);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                BaseDao.closeResources(connection, null, resultSet);
            }
        }
        return billList;
    }

    @Override
    public int getBillCountByProviderId(Connection connection, String providerId) throws SQLException {
        int count = 0;
        ResultSet resultSet = null;
        if (connection != null) {
            String sql = "select count(1) as billCount from smbms_bill where providerId = ?";
            Object[] params = {providerId};
            try {
                resultSet = BaseDao.execute(connection, null, sql, params, null);
                if (resultSet.next()) count = resultSet.getInt("billCount");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, null, resultSet);
            }
        }
        return count;
    }

    @Override
    public Bill getBillById(int id) throws SQLException {
        String sql = "select * from smbms_bill where id=?";
        Connection connection = BaseDao.getConnection();
        ResultSet resultSet = null;
        Bill bill = null;
        Object[] params = {id};
        if (connection != null) {
            try {
                resultSet = BaseDao.execute(connection, null, sql, params, null);
                while (resultSet.next()) {
                    bill = new Bill();
                    bill.setId(resultSet.getInt("id"));
                    bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                    bill.setProviderId(resultSet.getInt("providerId"));
                    bill.setProductName(resultSet.getString("productName"));
                    bill.setProductCount(resultSet.getBigDecimal("productCount"));
                    bill.setBillCode(resultSet.getString("billCode"));
                    bill.setIsPayment(resultSet.getInt("isPayment"));
                    bill.setProductUnit(resultSet.getString("productUnit"));
                    bill.setProductDesc(resultSet.getString("productDesc"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                BaseDao.closeResources(connection, null, resultSet);
            }
        }
        return bill;
    }

    @Override
    public boolean updateBill(Connection connection, Bill bill) throws SQLException {
        boolean flag = false;
        int count;
        if (connection != null) {
            String sql = "update smbms_bill set productName=?,productUnit=?,productCount=?,totalPrice=?," +
                    "providerId=?,isPayment=?,modifyBy=?,modifyDate=?,productDesc=? where id=?";
            Object[] params = {bill.getProductName(), bill.getProductUnit(), bill.getProductCount(),
                    bill.getTotalPrice(), bill.getProviderId(), bill.getIsPayment(), bill.getModifyBy(),
                    bill.getModifyDate(), bill.getProductDesc(), bill.getId()};
            count = BaseDao.execute(connection, null, sql, params);
            if (count > 0) {
                flag = true;
            }
            BaseDao.closeResources(null, null, null);
        }
        return flag;
    }

    @Override
    public boolean deleteBillById(Connection connection, int id) throws SQLException {
        boolean flag = false;
        String sql = "delete from smbms_bill where id=?";
        Object[] params = {id};
        int count;
        if (connection != null) {
            try {
                count = BaseDao.execute(connection, null, sql, params);
                if (count > 0) flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, null, null);
            }
        }
        return flag;
    }

    @Override
    public boolean add(Connection connection, Bill bill) throws SQLException {
        String sql = "insert into smbms_bill(billCode,productName,productDesc,productUnit,productCount," +
                "totalPrice,isPayment,createdBy,creationDate,providerId) values(?,?,?,?,?,?,?,?,?,?)";
        boolean flag = false;
        Object[] params = {bill.getBillCode(), bill.getProductName(), bill.getProductDesc(),
                bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                bill.getCreatedBy(), bill.getCreationDate(), bill.getProviderId()};
        int count;
        if (connection != null) {
            try {
                count = BaseDao.execute(connection, null, sql, params);
                if (count > 0) {
                    flag = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(null, null, null);
            }
        }
        return flag;
    }
}
