package com.nathan.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.nathan.dao.BaseDao;
import com.nathan.pojo.Role;
import com.nathan.pojo.User;
import com.nathan.service.user.UserService;
import com.nathan.service.user.UserServiceImp;
import com.nathan.service.userRole.UserRoleService;
import com.nathan.service.userRole.UserRoleServiceImp;
import com.nathan.util.Constant;
import com.nathan.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getParameter("method");
        if (method != null) {
            switch (method) {
                case "savepwd":
                    try {
                        this.updatePassword(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "pwdpassword":
                    this.confirmPassword(req, resp);
                    break;
                case "query":
                    try {
                        this.query(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "add":
                    this.add(req, resp);
                    break;
                case "getrolelist":
                    try {
                        this.getRoleList(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "ucexist":
                    try {
                        this.isUserExist(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "deluser":
                    try {
                        this.deleteUser(req, resp);
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
                case "modifyexe":
                    try {
                        this.modifySave(req, resp);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String newPassword = request.getParameter("newpassword");
        Object object = request.getSession().getAttribute(Constant.USER_SESSION);
        Connection connection = BaseDao.getConnection();
        int id;
        boolean flag;
        if (object != null && newPassword != null) {
            id = ((User) object).getId();
            UserService userService = new UserServiceImp();
            flag = userService.modifyPassword(connection, id, newPassword);
            if (flag) {
                request.getSession().setAttribute(Constant.MESSAGE, "Success");
                request.getSession().setAttribute(Constant.USER_SESSION, null);
            } else {
                request.getSession().setAttribute(Constant.MESSAGE, "Fail");
            }
        } else {
            request.setAttribute(Constant.MESSAGE, "Unsupported type");
        }
        try {
            request.getRequestDispatcher("pwdmodify.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void confirmPassword(HttpServletRequest request, HttpServletResponse response) {
        String oldPassword = request.getParameter("oldpassword");
        Object object = request.getSession().getAttribute(Constant.USER_SESSION);
        Map<String, String> resultMap = new HashMap<>();
        if (object == null) resultMap.put("result", "sessionError");
        else if (oldPassword == null) resultMap.put("result", "error");
        else {
            String userPassword = ((User) object).getUserPassword();
            if (userPassword.equals(oldPassword)) resultMap.put("result", "true");
            else resultMap.put("result", "false");
        }
        try {
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONArray.toJSONString(resultMap));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void query(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String queryUserName = request.getParameter("queryName");
        String userRole = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");
        int queryUserRole = 0;
        UserService userService = new UserServiceImp();
        List<User> userList;
        int pageSize = Constant.PAGE_SIZE;
        int currentPageNo = 1;
        if (queryUserName == null) queryUserName = "";
        if (userRole != null && !userRole.equals("")) queryUserRole = Integer.parseInt(userRole);
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
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        pageSupport.setCurrentPageNo(currentPageNo);
        int pageTotalCount = pageSupport.getTotalPageCount();
        if (currentPageNo < 1) currentPageNo = 1;
        else if (currentPageNo > pageTotalCount) currentPageNo = pageTotalCount;
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        request.setAttribute("userList", userList);
        List<Role> roleList;
        UserRoleService userRoleService = new UserRoleServiceImp();
        roleList = userRoleService.getRoleList();
        request.setAttribute("roleList", roleList);
        request.setAttribute("queryUserName", queryUserName);
        request.setAttribute("queryUserRole", queryUserRole);
        request.setAttribute("totalPageCount", pageTotalCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPageNo", currentPageNo);
        try {
            request.getRequestDispatcher("userlist.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        UserService userService = new UserServiceImp();
        int genderDefault = 1;
        Date birthdayDefault = null;
        int userRoleDefault = 3;
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");
        if (gender != null) {
            try {
                genderDefault = Integer.parseInt(gender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (birthday != null && !birthday.equals("")) {
            try {
                birthdayDefault = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (userRole != null && !userRole.equals("")) {
            try {
                userRoleDefault = Integer.parseInt(userRole.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        user.setUserCode(userCode);
        user.setUserRole(userRoleDefault);
        user.setBirthday(birthdayDefault);
        user.setAddress(address);
        user.setPhone(phone);
        user.setGender(genderDefault);
        user.setUserPassword(userPassword);
        user.setUserName(userName);
        user.setModifyBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getUserRole());
        user.setModifyDate(new Date());
        user.setCreatedBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getUserRole());
        user.setCreationDate(new Date());
        try {
            if (userService.addUser(user)) {
                response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
            } else {
                request.getRequestDispatcher("useradd.jsp").forward(request, response);
            }
        } catch (SQLException | IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public void getRoleList(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        UserRoleService userRoleService = new UserRoleServiceImp();
        List<Role> roleList = userRoleService.getRoleList();
        response.setContentType("application/json");
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONArray.toJSONString(roleList));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            request.getRequestDispatcher("useradd.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isUserExist(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String userCode = request.getParameter("userCode");
        Map<String, String> resultMap = new HashMap<>();
        UserService userService = new UserServiceImp();
        if (userService.isUserExist(userCode)) {
            resultMap.put("userCode", "Exist");
        } else {
            resultMap.put("userCode", "NotExist");
        }
        response.setContentType("application/json");
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONArray.toJSONString(resultMap));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String uid = request.getParameter("uid");
        Map<String, String> resultMap = new HashMap<>();
        int id;
        if (uid != null) {
            id = Integer.parseInt(uid);
            UserService userService = new UserServiceImp();
            if (userService.deleteUser(id)) {
                resultMap.put("delResult", "True");
            } else {
                resultMap.put("delResult", "False");
            }
        } else {
            resultMap.put("delResult", "Empty");
        }
        response.setContentType("application/json");
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONArray.toJSONString(resultMap));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("userlist.jsp");
    }

    public void view(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        User user;
        String uid = request.getParameter("uid");
        int id = -1;
        if (uid != null && !uid.equals("")) id = Integer.parseInt(uid);
        UserService userService = new UserServiceImp();
        user = userService.viewUser(id);
        if (user != null) {
            request.setAttribute("user", user);
            try {
                request.getRequestDispatcher("userview.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                request.getRequestDispatcher("userlist.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void modify(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String uid = request.getParameter("uid");
        int id = -1;
        User user;
        UserService userService = new UserServiceImp();
        if (uid != null && !uid.equals("")) id = Integer.parseInt(uid);
        user = userService.viewUser(id);
        if (user != null) {
            user.setId(id);
            try {
                request.getSession().setAttribute("user", user);
                request.getRequestDispatcher("usermodify.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                request.getRequestDispatcher("userlist.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void modifySave(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");
        User user = (User) request.getSession().getAttribute("user");
        int genderDefault = user.getGender();
        int userRoleDefault = user.getUserRole();
        if (gender != null && !gender.equals("")) genderDefault = Integer.parseInt(gender);
        if (userRole != null && !userRole.equals("")) userRoleDefault = Integer.parseInt(userRole);
        user.setUserRole(userRoleDefault);
        user.setAddress(address);
        user.setUserName(userName);
        user.setPhone(phone);
        user.setGender(genderDefault);
        user.setModifyDate(new Date());
        user.setModifyBy(((User) request.getSession().getAttribute(Constant.USER_SESSION)).getUserRole());
        UserService userService = new UserServiceImp();
        if (userService.modifyUser(user)) {
            try {
                response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
            } catch (Exception e) {
                request.setAttribute(Constant.MESSAGE, "Fail");
                e.printStackTrace();
            }
        } else {
            request.setAttribute(Constant.MESSAGE, "Fail");
            try {
                request.getRequestDispatcher("usermodify.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
