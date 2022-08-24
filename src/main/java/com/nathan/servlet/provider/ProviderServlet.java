package com.nathan.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.nathan.pojo.Provider;
import com.nathan.pojo.User;
import com.nathan.service.provider.ProviderService;
import com.nathan.service.provider.ProviderServiceImp;
import com.nathan.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void destroy(){
        super.destroy();
    }

    public void modify(HttpServletRequest request,
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
        provider.setModifyBy(((User)request.getSession().getAttribute(Constant.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        ProviderService providerService = new ProviderServiceImp();
        boolean flag = false;
        flag = providerService.modify(provider);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
        }else {
            request.getRequestDispatcher("providermodify.jsp").forward(request,response);
        }
    }

    public void deleteProvider(HttpServletRequest request,
                               HttpServletResponse response) throws SQLException, IOException, ServletException {
        String providerId = request.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(providerId)){
            ProviderService providerService = new ProviderServiceImp();
            int flag = providerService.deleteProviderById(providerId);
            if (flag==0) resultMap.put("deleteResult","True");
            else if (flag==-1) resultMap.put("deleteResult","False");
            else if(flag>0) resultMap.put("deleteResult", String.valueOf(flag));
        }else {
            resultMap.put("deleteResult", "Error");
        }
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONArray.toJSONString(resultMap));
        printWriter.flush();
        printWriter.close();
        request.getRequestDispatcher("providerlist.jsp").forward(request,response);
    }
}
