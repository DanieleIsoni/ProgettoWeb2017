<%-- 
    Document   : changepassword
    Created on : 18-ott-2017, 21.00.23
    Author     : massimo
    This JSP show the user the form to change his password after forgotten and requested the recovery
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>
<!DOCTYPE html>
<html>
    <head>
        
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="changepassword_title"/> - BuyHub</title>
    </head>
    <body>
        
        <div class="text-center login">
        <%@include file="common/navbar.jsp" %>
        <img src="images/icon.png" alt="BuyHub logo" height="42" width="42">
        <h3><fmt:message key="changepassord_desc"/></h3>
        
        
        <div class="panel panel-default panel-footer">
            <form method="POST" id="login-form" action="<c:url value="/changePassword" />">
                
        <err:ErrorMessage page="changepassword"/>
                <div class="form-group">
                    <label>Password:</label>
                    <input type="password" name="password" class="form-control" id="password">
                </div>
                <div class="form-group">
                    <label><fmt:message key="repeat_password"/>:</label>
                    <input type="password" name="password2" class="form-control" id="password2">
                </div>
                    <input type="hidden" name="token" class="form-control" id="token" value="${param['token']}">
                <button type="submit" class="btn btn-success"><fmt:message key="confirm"/></button>
            </form>
        </div>
        </div>
        <%@include file="common/footer.jsp" %>
