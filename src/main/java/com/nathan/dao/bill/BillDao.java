package com.nathan.dao.bill;

import com.nathan.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    int getCountByCondition(String queryProductionName, Integer isPayment, Integer providerId) throws SQLException;

    List<Bill> getBillListByCondition(String queryProductionName, Integer isPayment, Integer providerId,
                                             Integer currentPageNo, Integer pageSize) throws SQLException;

    int getBillCountByProviderId(Connection connection, String providerId) throws SQLException;

    Bill getBillById(int id) throws SQLException;

    boolean updateBill(Connection connection, Bill bill) throws SQLException;

    boolean deleteBillById(Connection connection, int id) throws SQLException;

    boolean add(Connection connection, Bill bill) throws SQLException;
}
