package com.nathan.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.nathan.pojo.Provider;
import com.nathan.pojo.User;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {

        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {
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
                case "add":
                    try {
                        this.add(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "view":
                    try {
                        this.getProviderById(req, resp, "providerview.jsp");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "modify":
                    try {
                        this.getProviderById(req, resp, "providermodify.jsp");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "modifysave":
                    try {
                        this.modify(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "delprovider":
                    try {
                        this.deleteProvider(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    }

    private void query(HttpServletRequest request,
                       HttpServletResponse response) throws SQLException, ServletException, IOException {
        String queryProName = request.getParameter("queryProName");
        String queryProCode = request.getParameter("queryProCode");
        String pageIndex = request.getParameter("pageIndex");
        if (!StringUtils.isNullOrEmpty(queryProName)) {
            queryProName = "";
        }
        if (!StringUtils.isNullOrEmpty(queryProCode)) {
            queryProCode = "";
        }
        List<Provider> providerList;
        ProviderService providerService = new ProviderServiceImp();
        int pageSize = Constant.PAGE_SIZE;
        int currentPageNo = 1;
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            } catch (Exception e) {
                try {
                    response.sendRedirect("error.jsp");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        int totalCount = providerService.getProviderCount(queryProName, queryProCode);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        if (currentPageNo < 1) currentPageNo = 1;
        else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        providerList = providerService.getProviderList(queryProName, queryProCode, currentPageNo, pageSize);
        request.setAttribute("providerList", providerList);
        request.setAttribute("queryProName", queryProName);
        request.setAttribute("queryProCode", queryProCode);
        request.setAttribute("totalPageCount", totalPageCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPageNo", currentPageNo);
        request.getRequestDispatcher("providerlist.jsp").forward(request, response);
    }

    private void add(HttpServletRequest request,
                     HttpServletResponse response) throws SQLException, IOException, ServletException {
        String proCode = request.getParameter("proCode");
        String proName = request.getParameter("proName");
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        ProviderService providerService = new ProviderServiceImp();
        boolean flag;
        flag = providerService.add(provider);
        if (flag) {
            response.sendRedirect(request.getContextPath() + "/jsp/provider.do?method=query");
        } else {
            request.getRequestDispatcher("provideradd.jsp").forward(request, response);
        }
    }

    private void getProviderById(HttpServletRequest request, HttpServletResponse response,
                                 String url) throws SQLException, ServletException, IOException {
        String id = request.getParameter("proid");
        if (!StringUtils.isNullOrEmpty(id)) {
            ProviderService providerService = new ProviderServiceImp();
            Provider provider = providerService.getProviderById(id);
            request.setAttribute("provider", provider);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    public void destroy() {
        super.destroy();
    }

    private void modify(HttpServletRequest request,
                        HttpServletResponse response) throws SQLException, IOException, ServletException {
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");
        String id = request.getParameter("id");
        String proName = request.getParameter("proName");
        Provider provider = new Provider();
        provider.setId(Integer.valueOf(id));
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setProName(proName);
        provider.setModifyBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        ProviderService providerService = new ProviderServiceImp();
        boolean flag;
        flag = providerService.modify(provider);
        if (flag) {
            response.sendRedirect(request.getContextPath() + "/jsp/provider.do?method=query");
        } else {
            request.getRequestDispatcher("providermodify.jsp").forward(request, response);
        }
    }

    private void deleteProvider(HttpServletRequest request,
                                HttpServletResponse response) throws SQLException, IOException, ServletException {
        String providerId = request.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(providerId)) {
            ProviderService providerService = new ProviderServiceImp();
            int flag = providerService.deleteProviderById(providerId);
            if (flag == 0) resultMap.put("deleteResult", "True");
            else if (flag == -1) resultMap.put("deleteResult", "False");
            else if (flag > 0) resultMap.put("deleteResult", String.valueOf(flag));
        } else {
            resultMap.put("deleteResult", "Error");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONArray.toJSONString(resultMap));
        printWriter.flush();
        printWriter.close();
        request.getRequestDispatcher("providerlist.jsp").forward(request, response);
    }
}
