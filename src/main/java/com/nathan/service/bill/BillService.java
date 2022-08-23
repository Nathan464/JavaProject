package com.nathan.service.bill;

import com.nathan.pojo.Bill;

import java.sql.SQLException;
import java.util.List;

public interface BillService {
    int getCountByCondition(String queryProductName, Integer isPayment, Integer providerId) throws SQLException;

    List<Bill> getBillListByCondition(String queryProductName, Integer isPayment, Integer providerId,
                                      Integer currentPageNo, Integer pageSize) throws SQLException;

    int getBillCountByProviderId(String providerId) throws SQLException;

    Bill getBillById(int id) throws SQLException;

    boolean updateBill(Bill bill) throws SQLException;

    boolean deleteBillById(int id) throws SQLException;

    boolean add(Bill bill) throws SQLException;
}
