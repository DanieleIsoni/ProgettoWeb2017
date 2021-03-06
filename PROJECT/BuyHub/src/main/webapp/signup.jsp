<%-- 
    Document   : signup
    Created on : 6-lug-2017, 14.55.33
    Author     : Matteo Battilana
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>
<!DOCTYPE html>
<html>
    <head>

        <%@include file="common/header.jsp" %>
        <title><fmt:message key="signup_title"/> - BuyHub</title>
    </head>
    <body>

        <%@include file="common/navbar.jsp" %>
        <div class="text-center login">
            <img src="images/icon.png" alt="BuyHub logo" height="42" width="42">
            <h3><fmt:message key="signup_desc"/></h3>


            <div class="panel panel-default panel-footer">
                <form method="POST" id="login-form" action="<c:url value="/signup" />">

                    <err:ErrorMessage page="login"/>
                    <div class="form-group">
                        <label for="first_name"><fmt:message key="first_name"/>:</label>
                        <input type="text" name="first_name" class="form-control" id="first_name">
                    </div>
                    <div class="form-group">
                        <label for="last_name"><fmt:message key="last_name"/>:</label>
                        <input type="text" name="last_name" class="form-control" id="last_name">
                    </div>
                    <div class="form-group">
                        <label for="email"><fmt:message key="email_address"/>:</label>
                        <input type="email" name="email" class="form-control" id="email">
                    </div>
                    <div class="form-group">
                        <label for="username"><fmt:message key="username"/>:</label>
                        <input type="text" name="username" class="form-control" id="username">
                    </div>
                    <div class="form-group">
                        <label>Password:</label>
                        <input type="password" name="password" class="form-control" id="password">
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="repeat_password"/>:</label>
                        <input type="password" name="password2" class="form-control" id="password2">
                    </div>
                    <div class="form-group">
                        <label class="form-check-label">
                            <input type="checkbox" name="conditions" class="form-check-input" id="conditions" required> <a href="disclaimer.jsp"><fmt:message key="accept_conditions"/></a>
                        </label>
                    </div>

                    <button type="submit" class="btn btn-success"><fmt:message key="sign_up"/></button>
                </form>
            </div>
        </div>

        <%@include file="common/footer.jsp" %>
