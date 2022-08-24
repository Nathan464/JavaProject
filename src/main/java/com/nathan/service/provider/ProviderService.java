package com.nathan.service.provider;

import com.nathan.pojo.Provider;

import java.util.List;

public interface ProviderService {
    boolean add(Provider provider);

    List<Provider> getProviderList(String proName, String proCode, Integer currentPageNo, Integer pageSize);

    int getProviderCount(String proName, String proCode);

    int deleteProviderById(String providerId);

    Provider getProviderById(String providerId);

    boolean modify(Provider provider);
}
