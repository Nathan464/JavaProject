<%--
  Created by IntelliJ IDEA.
  User: l1979
  Date: 2022/8/22
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Login - Supermarket Management System</title>
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css"/>
    <script type="text/javascript"></script>
</head>
<body class="login_bg">
    <section class="loginBox">
        <header class="loginHeader">
            <h1 style="text-align: center">Supermarket Management System</h1>
        </header>
        <section class="loginCont">
            <form class="loginForm"
                  action="${pageContext.request.contextPath}/login.do"
                  name="actionForm" id="actionForm" method="post">
                <div class="info">${error}</div>
                <div class="inputBox">
                    <label for="userCode">User:</label>
                    <input type="text" class="input-text" id="userCode"
                           placeholder="Enter Username" required/>
                </div>
                <div class="inputBox">
                    <label for="userPassword">Password:</label>
                    <input type="text" id="userPassword" name="userPassword"
                           placeholder="Enter Password" required/>
                </div>
                <div class="subBtn">
                    <input type="submit" value="Login"/>
                    <input type="reset" value="Reset"/>
                </div>
            </form>
        </section>
    </section>
</body>
</html>
