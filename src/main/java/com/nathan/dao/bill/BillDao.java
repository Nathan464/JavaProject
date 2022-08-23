package com.nathan.dao.bill;

import com.nathan.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    public int getCountByCondition(String queryProductionName, Integer isPayment, Integer providerId) throws SQLException;

    public List<Bill> getBillListByCondition(String queryProductionName, Integer isPayment, Integer providerId,
                                             Integer currentPageNo, Integer pageSize) throws SQLException;

    public int getBillCountByProviderId(Connection connection, String providerId) throws SQLException;

    public Bill getBillById(int id) throws SQLException;

    public boolean updateBill(Connection connection, Bill bill) throws SQLException;

    public boolean deleteBillById(Connection connection, int id) throws SQLException;

    public boolean add(Connection connection, Bill bill) throws SQLException;
}
