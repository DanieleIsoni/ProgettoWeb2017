<%-- 
    Document   : error
    Created on : 3-lug-2017, 18.43.35
    Author     : matteo

<%@include file="header.jsp" %>
       
--%>

<%@ page isErrorPage="true" %>  
<%@include file="header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="error_title"/></title>
    </head>
    <body>
        <div class="error-container" style="background-image: url('<c:url value="/images/errorPage/pattern-night.png" />');">
            <div id="background-cloud" style="background-image: url('<c:url value="/images/errorPage/pattern-cloud.png" />');"> </div>
            <img id="rocket" src="<c:url value="/images/errorPage/rocket.png" />" />
            <img id="e404" src="<c:url value="/images/errorPage/404.png" />" />
        </div>

        <div id="suggestions">
            <a href="https://github.com/contact">Contact Support</a> -
            <a href="https://status.github.com">GitHub Status</a> -
            <a href="https://twitter.com/githubstatus">@githubstatus</a>
        </div>

    </body>
</html>