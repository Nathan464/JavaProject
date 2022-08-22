package com.nathan.servlet.user;

import com.nathan.pojo.User;
import com.nathan.service.user.UserService;
import com.nathan.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String password = req.getParameter("password");
        UserService userService = new UserService();
        User user = userService.login(userCode, password);
        if (user != null) {
            req.getSession().setAttribute(Constant.USER_SESSION, user);
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            req.setAttribute("error", "Invalid username or password!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
