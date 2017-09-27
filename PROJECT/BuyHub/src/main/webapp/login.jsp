<%-- 
    Document   : login
    Created on : 3-lug-2017, 13.46.26
    Author     : matteo5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>

        <title><fmt:message key="login_title"/> - BuyHub</title>
    </head>
    <body class="text-center login">
        
        <%@include file="common/navbar.jsp" %>
        <img src="images/icon.png" alt="BuyHub logo" height="42" width="42">
        <h3><fmt:message key="login_desc"/></h3>
        <br>
        
        <div class="panel panel-default panel-footer">
            
            <err:ErrorMessage page="login"/>
            <form method="POST" id="login-form" action="<c:url value="/login" />">
                <div class="form-group">
                    <label for="username"><fmt:message key="username"/>:</label>
                    <input type="text" name="username" class="form-control" id="username">
                </div>
                <div class="form-group">
                    <label>Password:</label><a href="#" class="to-right site-header-link2"><fmt:message key="forgot_password"/></a>
                    <input type="password" name="password" class="form-control" id="password">
                </div>
                    
                <button type="submit" class="btn btn-success"><fmt:message key="login"/></button>
                <a href="<c:url value="/signup.jsp" />" class="btn btn-default" role="button"><fmt:message key="sign_up"/></a>
            </form>
        </div>

 <%@include file="common/footer.jsp" %>