<%-- 
    Document   : requestpassword
    Created on : 18-ott-2017, 21.37.24
    Author     : massimo
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>
<!DOCTYPE html>
<html>
    <head>
        
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="requestpassword_title"/> - BuyHub</title>
    </head>
    <body>
        
        <div class="text-center login">
        <%@include file="common/navbar.jsp" %>
        <img src="images/icon.png" alt="BuyHub logo" height="42" width="42">
        <h3><fmt:message key="requestpassword_title"/></h3>
        <h4><fmt:message key="requestpassword_desc"/></h4>
        
        
        <div class="panel panel-default panel-footer">
            <form method="GET   " id="login-form" action="<c:url value="/requestPasswordChange" />">
                
        <err:ErrorMessage page="requestpassword"/>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" class="form-control" id="email">
                </div>
                <button type="submit" class="btn btn-success"><fmt:message key="confirm"/></button>
            </form>
        </div>
        </div>
        <%@include file="common/footer.jsp" %>
