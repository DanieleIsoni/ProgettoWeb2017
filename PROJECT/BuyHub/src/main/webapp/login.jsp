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
            <form id="login-form">
                <div class="form-group">
                    <label for="email">Email address:</label>
                    <input type="email" class="form-control" id="email">
                </div>
                <div class="form-group">
                    <label>Password:</label><a href="#" class="to-right site-header-link2"><fmt:message key="forgot_password"/></a>
                    <input type="password" class="form-control" id="pwd">
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
