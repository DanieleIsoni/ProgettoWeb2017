<%-- 
    Document   : login
    Created on : 3-lug-2017, 13.46.26
    Author     : matteo5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/navbar.jsp" %>

        <title><fmt:message key="login_title"/></title>
    </head>
    <body class="text-center login">
        <img src="images/icon.png" alt="BuyHub logo" height="42" width="42">
        <h3><fmt:message key="login_desc"/></h3>
        <div class="panel panel-default panel-footer">
            <form method="POST" id="login-form" action="<c:url value="/LoginServlet" />">
                <div class="form-group">
                    <label for="username"><fmt:message key="username"/>:</label>
                    <input type="text" name="username" class="form-control" id="username">
                </div>
                <div class="form-group">
                    <label>Password:</label><a href="#" class="to-right site-header-link2"><fmt:message key="forgot_password"/></a>
                    <input type="password" name="password" class="form-control" id="password">
                </div>
                <div class="checkbox">
                    <label><input type="checkbox"><fmt:message key="remember_me"/></label>
                </div>
                <button type="submit" class="btn btn-success"><fmt:message key="login"/></button>
                <button type="submit" class="btn btn-default"><fmt:message key="sign_up"/></button>
            </form>
        </div>
    </body>
</html>
