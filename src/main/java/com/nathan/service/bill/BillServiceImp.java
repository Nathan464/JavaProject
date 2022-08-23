package com.nathan.service.bill;

import com.nathan.dao.BaseDao;
import com.nathan.dao.bill.BillDao;
import com.nathan.dao.bill.BillDaoImp;
import com.nathan.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImp implements BillService {
    private final BillDao billDao;

    public BillServiceImp() {
        billDao = new BillDaoImp();
    }

    @Override
    public int getCountByCondition(String queryProductName, Integer isPayment,
                                   Integer providerId) throws SQLException {
        return billDao.getCountByCondition(queryProductName, isPayment, providerId);
    }

    @Override
    public List<Bill> getBillListByCondition(String queryProductName, Integer isPayment,
                                             Integer providerId, Integer currentPageNo,
                                             Integer pageSize) throws SQLException {
        return billDao.getBillListByCondition(queryProductName, isPayment, providerId, currentPageNo, pageSize);
    }

    @Override
    public int getBillCountByProviderId(String providerId) throws SQLException {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = billDao.getBillCountByProviderId(connection, providerId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return count;
    }

    @Override
    public Bill getBillById(int id) throws SQLException {
        return billDao.getBillById(id);
    }

    @Override
    public boolean updateBill(Bill bill) throws SQLException {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;
        if (connection!=null){
            try {
                connection.setAutoCommit(false);
                flag = billDao.updateBill(connection,bill);
                connection.commit();
            }catch (SQLException e){
                try {
                    connection.rollback();
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }

        }
        return flag;
    }

    @Override
    public boolean deleteBillById(int id) throws SQLException {
        boolean flag = false;
        Connection connection = BaseDao.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
                flag = billDao.deleteBillById(connection,id);
                connection.commit();
            }catch (SQLException e){
                flag = false;
                try {
                    connection.rollback();
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                BaseDao.closeResources(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public boolean add(Bill bill) throws SQLException {
        boolean flag = false;
        Connection connection = BaseDao.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
                flag = billDao.add(connection, bill);
                connection.commit();
            } catch (SQLException e) {
                flag = false;
                try {
                    connection.rollback();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                BaseDao.closeResources(connection, null, null);
            }
        }
        return flag;
    }
}
