<%-- 
    Document   : cart
    Created on : 8-lug-2017, 20.55.51
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/navbar.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

    </head>
    <body>
        <c:forEach items="${userCart.products}" var="pr">
            <h3><c:out value="${pr.id}"/> - <c:out value="${pr.number}"/></h3>
        </c:forEach>
    </body>
</html>
