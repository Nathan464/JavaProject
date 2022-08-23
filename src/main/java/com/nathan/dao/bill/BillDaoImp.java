package com.nathan.dao.bill;

import com.nathan.pojo.Bill;

import java.sql.Connection;
import java.util.List;

public class BillDaoImp implements BillDao{

    @Override
    public int getCountByCondition(String queryProductionName, Integer isPayment, Integer providerId) {
        return 0;
    }

    @Override
    public List<Bill> getBillListByCondition(String queryProductionName, Integer isPayment, Integer providerId, Integer currentPageNo, Integer pageSize) {
        return null;
    }

    @Override
    public int getBillCountByProviderId(Connection connection, String providerId) {
        return 0;
    }

    @Override
    public Bill getBillById(int id) {
        return null;
    }

    @Override
    public boolean updateBill(Connection connection, Bill bill) {
        return false;
    }

    @Override
    public boolean deleteBillById(Connection connection, int id) {
        return false;
    }

    @Override
    public boolean add(Connection connection, Bill bill) {
        return false;
    }
}
