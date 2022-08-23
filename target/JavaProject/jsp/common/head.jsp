<%--
  Created by IntelliJ IDEA.
  User: l1979
  Date: 2022/8/23
  Time: 9:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Supermarket Management System</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/public.css"/>
</head>
<body>
    <header class="publicHeader">
        <h1 style="text-align: center">Supermarket Management System</h1>
        <div class="publicHeaderR">
            <p>Welcome, <span style="color: #fff21b">${userSession.userName}</span></p>
            <a href="${pageContext.request.contextPath}/logout.do">Logout</a>
        </div>
    </header>
    <section class="publicMian ">
        <div class="left">
            <h2 class="leftH2"><span class="span1"></span>功能列表 <span></span></h2>
            <nav>
                <ul class="list">
                    <li ><a href="${pageContext.request.contextPath }/jsp/bill?method=query">Order</a></li>
                    <li><a href="${pageContext.request.contextPath }/jsp/provider?method=query">Supplier</a></li>
                    <li><a href="${pageContext.request.contextPath }/jsp/user?method=query">User</a></li>
                    <li><a href="${pageContext.request.contextPath }/jsp/pwdmodify">Password</a></li>
                    <li><a href="${pageContext.request.contextPath }/logout.do">Logout</a></li>
                </ul>
            </nav>
        </div>
        <input type="hidden" id="path" name="path" value="${pageContext.request.contextPath }"/>
        <input type="hidden" id="referer" name="referer" value="<%=request.getHeader("Referer")%>"/>
    </section>
</body>
</html>
