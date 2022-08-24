package com.nathan.service.provider;

import com.nathan.dao.bill.BillDao;
import com.nathan.dao.bill.BillDaoImp;
import com.nathan.dao.provider.ProviderDao;
import com.nathan.dao.provider.ProviderDaoImp;
import com.nathan.pojo.Provider;

import java.util.List;

public class ProviderServiceImp implements ProviderService{
    private final ProviderDao providerDao;
    private final BillDao billDao;
    public ProviderServiceImp(){
        providerDao = new ProviderDaoImp();
        billDao = new BillDaoImp();
    }
    @Override
    public boolean add(Provider provider) {

        return false;
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode, Integer currentPageNo, Integer pageSize) {
        return null;
    }

    @Override
    public int getProviderCount(String proName, String proCode) {
        return 0;
    }

    @Override
    public int deleteProviderById(String providerId) {
        return 0;
    }

    @Override
    public Provider getProviderById(String providerId) {
        return null;
    }

    @Override
    public boolean modify(Provider provider) {
        return false;
    }
}
