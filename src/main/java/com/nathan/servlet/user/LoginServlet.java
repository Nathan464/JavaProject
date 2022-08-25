package com.nathan.servlet.user;

import com.nathan.pojo.User;
import com.nathan.service.user.UserService;
import com.nathan.service.user.UserServiceImp;
import com.nathan.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String password = req.getParameter("userPassword");
        UserService userService = new UserServiceImp();
        User user;
        try {
            user = userService.login(userCode, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            req.getSession().setAttribute(Constant.USER_SESSION, user);
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            req.setAttribute("Error", "Wrong userName or Password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
