<%-- 
    Document   : cart
    Created on : 8-lug-2017, 20.55.51
    Author     : matteo
--%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="cart_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="common/navbar.jsp" %>
        <c:forEach items="${userCart.products}" var="pr">
            <h3><c:out value="${pr.id}"/> - <c:out value="${pr.number}"/></h3>
        </c:forEach>
<%@include file="common/footer.jsp" %>
        