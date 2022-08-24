package com.nathan.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.nathan.pojo.Bill;
import com.nathan.pojo.Provider;
import com.nathan.pojo.User;
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
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method!=null){
            switch (method) {
                case "query":
                    try {
                        this.query(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "view":
                    try {
                        this.view(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "modify":
                    try {
                        this.modify(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "modifySave":
                    try {
                        this.modifySave(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "deleteBill":
                    try {
                        this.deleteBill(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "add":
                    try {
                        this.add(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "getProviderList":
                    try {
                        this.getProviderListServlet(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void getProviderListServlet(HttpServletRequest request, HttpServletResponse response) throws SQLException{
        ProviderService providerService = new ProviderServiceImp();
        List<Provider> providerList = providerService.getProviderList(null,null,
                null,null);
        try {
            request.getSession().setAttribute("providerLit",providerList);
            request.getRequestDispatcher("billadd.jsp").forward(request,response);
        }catch (ServletException|IOException exception){
            exception.printStackTrace();
        }
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String billCode = request.getParameter("billCode");
        int providerId = Integer.parseInt(request.getParameter("providerId"));
        int isPayment = Integer.parseInt(request.getParameter("isPayment"));
        String productDescribe = request.getParameter("productDescribe");
        String productName = request.getParameter("productName");
        String productUnit = request.getParameter("productUnit");
        BigDecimal productCount = new BigDecimal(request.getParameter("productCount"));
        BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice"));
        Bill bill = new Bill();
        bill.setCreatedBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getUserRole());
        bill.setCreationDate(new Date());
        bill.setProductName(productName);
        bill.setProviderId(providerId);
        bill.setTotalPrice(totalPrice);
        bill.setIsPayment(isPayment);
        bill.setProductUnit(productUnit);
        bill.setProductCount(productCount);
        bill.setBillCode(billCode);
        bill.setProductDesc(productDescribe);
        BillService billService = new BillServiceImp();
        if (billService.add(bill)) {
            try {
                response.sendRedirect(request.getContextPath() + "/jsp/bill.do?method=query");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                request.setAttribute(Constant.MESSAGE, "Fail");
                request.getRequestDispatcher("billadd.jsp").forward(request, response);
            } catch (ServletException | IOException exception){
                exception.printStackTrace();
            }
        }
    }

    public void deleteBill(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Map<String, String> deleteResult = new HashMap<>();
        int billId = -1;
        if (request.getParameter("billid") != null) {
            billId = Integer.parseInt(request.getParameter("billid"));
        }
        if (billId < 0) {
            deleteResult.put("deleteResult", "Empty");
        } else {
            BillService billService = new BillServiceImp();
            if (billService.deleteBillById(billId)) {
                deleteResult.put("deleteResult", "True");
            } else {
                deleteResult.put("deleteResult", "False");
            }
        }
        response.setContentType("application/json");
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONArray.toJSONString(deleteResult));
            printWriter.flush();
            printWriter.close();
            request.getRequestDispatcher("billlist.jsp?method=query").forward(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifySave(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String productName = request.getParameter("productName");
        String productUnit = request.getParameter("productUnit");
        BigDecimal productCount = new BigDecimal(request.getParameter("productCount"));
        BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice"));
        int providerId = Integer.parseInt(request.getParameter("providerId"));
        int isPayment = Integer.parseInt(request.getParameter("isPayment"));
        String productDescribe = request.getParameter("productDescribe");
        Bill bill = (Bill) request.getSession().getAttribute("bill");
        bill.setIsPayment(isPayment);
        bill.setProductUnit(productUnit);
        bill.setTotalPrice(totalPrice);
        bill.setProviderId(providerId);
        bill.setProductCount(productCount);
        bill.setProviderName(productName);
        bill.setId(bill.getId());
        bill.setProductDesc(productDescribe);
        bill.setModifyDate(new Date());
        bill.setModifyBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getId());
        BillService billService = new BillServiceImp();
        if (billService.updateBill(bill)) {
            try {
                request.getRequestDispatcher("/jsp/bill.do?method=query").forward(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                request.setAttribute(Constant.MESSAGE, "Fail");
                request.getRequestDispatcher("billmodify.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void modify(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String temp = request.getParameter("billid");
        Bill bill;
        int billId = -1;
        if (temp != null) billId = Integer.parseInt(temp);
        BillService billService = new BillServiceImp();
        bill = billService.getBillById(billId);
        ProviderService providerService = new ProviderServiceImp();
        bill.setProviderName(providerService.getProviderById("" + bill.getProviderId()).getProName());
        List<Provider> providerList = providerService.getProviderList(null,
                null, null, null);
        try {
            request.getSession().setAttribute("providerList", providerList);
            request.getSession().setAttribute("bill", bill);
            response.sendRedirect("billmodify.jsp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void view(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String temp = request.getParameter("billid");
        Bill bill;
        int billId = -1;
        if (temp != null) billId = Integer.parseInt(temp);
        BillService billService = new BillServiceImp();
        ProviderService providerService = new ProviderServiceImp();
        bill = billService.getBillById(billId);
        bill.setProviderName(providerService.getProviderById("" + bill.getProviderId()).getProName());
        try {
            request.getSession().setAttribute("bill", bill);
            response.sendRedirect(request.getContextPath() + "/jsp/billview.jsp");
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
