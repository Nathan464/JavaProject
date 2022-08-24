package com.nathan.servlet.bill;

import com.nathan.pojo.Bill;
import com.nathan.pojo.Provider;
import com.nathan.service.bill.BillService;
import com.nathan.service.bill.BillServiceImp;
import com.nathan.service.provider.ProviderService;
import com.nathan.service.provider.ProviderServiceImp;
import com.nathan.util.Constant;
import com.nathan.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


    public void view(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String temp = request.getParameter("billid");
        Bill bill = null;
        int billId = -1;
        if (temp != null) billId = Integer.parseInt(temp);
        BillService billService = new BillServiceImp();
        ProviderService providerService = new ProviderServiceImp();
        bill = billService.getBillById(billId);
        bill.setProductName(providerService.getProviderById("" + bill.getProviderId()).getProName());
        try {
            request.getSession().setAttribute("bill",bill);
            response.sendRedirect(request.getContextPath()+"/jsp/billview.jsp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void query(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        ProviderService providerService = new ProviderServiceImp();
        List<Provider> providerList = providerService.getProviderList(
                null, null, null, null);
        List<Bill> billList;
        String queryProductName = request.getParameter("queryProductName");
        String queryProviderId = request.getParameter("queryProviderId");
        String queryIsPayment = request.getParameter("queryIsPayment");
        String pageIndex = request.getParameter("pageIndex");
        Integer providerId = null;
        Integer isPayment = null;
        int currentPageNo = 1;
        int totalPageCount;
        int totalCount;
        int pageSize = Constant.PAGE_SIZE;
        if (queryProductName == null) queryProductName = "";
        if (queryProviderId != null) {
            providerId = Integer.valueOf(queryProviderId);
            if (providerId == 0) providerId = null;
        }
        if (queryIsPayment != null) {
            isPayment = Integer.valueOf(queryIsPayment);
            if (isPayment == 0) isPayment = null;
        }
        if (pageIndex != null) currentPageNo = Integer.parseInt(pageIndex);
        BillService billService = new BillServiceImp();
        totalCount = billService.getCountByCondition(queryProductName, isPayment, providerId);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        totalPageCount = pageSupport.getTotalPageCount();
        if (currentPageNo > totalPageCount) currentPageNo = totalPageCount;
        else if (currentPageNo <= 0) currentPageNo = 1;

        ProviderService providerServiceOne = new ProviderServiceImp();
        billList = billService.getBillListByCondition(queryProductName, isPayment, providerId,
                currentPageNo, pageSize);
        for (Bill bill : billList) {
            bill.setProductName(providerServiceOne.getProviderById("" + bill.getProviderId()).getProName());
        }
        try {
            request.setAttribute("billList", billList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalPageCount", totalPageCount);
            request.setAttribute("currentPageNo", currentPageNo);
            request.getSession().setAttribute("providerList", providerList);
            request.getRequestDispatcher("billlist.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
