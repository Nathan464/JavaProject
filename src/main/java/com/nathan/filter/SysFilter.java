package com.nathan.filter;

import com.nathan.pojo.User;
import com.nathan.util.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute(Constant.USER_SESSION);
        if (user==null){
            response.sendRedirect("error.jsp");
        }else {
            filterChain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {
    }
}
