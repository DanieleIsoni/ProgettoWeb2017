<%-- 
    Document   : myorders.jsp
    Created on : 13-dic-2017, 18.12.44
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../common/header.jsp" %>
        <%@taglib prefix="notification" uri="../WEB-INF/tld/notification.tld" %>


        <title><fmt:message key="notification_title"/> - BuyHub</title>
    </head>
    <body >
        <%@include file="../common/navbar.jsp" %>
        <div class="container" >
            <h2><fmt:message key="notification_title" /></h2>
            <notification:NotificationList />
        </div>


        <%@include file="../common/footer.jsp" %>