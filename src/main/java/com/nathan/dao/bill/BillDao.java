package com.nathan.dao.bill;

import com.nathan.pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillDao {
    public int getCountByCondition(String queryProductionName, Integer isPayment, Integer providerId);

    public List<Bill> getBillListByCondition(String queryProductionName, Integer isPayment, Integer providerId,
                                             Integer currentPageNo, Integer pageSize);

    public int getBillCountByProviderId(Connection connection, String providerId);

    public Bill getBillById(int id);

    public boolean updateBill(Connection connection, Bill bill);

    public boolean deleteBillById(Connection connection, int id);

    public boolean add(Connection connection, Bill bill);
}
