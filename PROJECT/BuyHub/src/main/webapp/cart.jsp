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

        <div class="container">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#"><fmt:message key="cart_title"/> <span class="badge"><c:out value="${userCart.getCount()}"/></span></a></li>

            </ul>

            <c:forEach items="${userCart.products}" var="pr">
                <h3><c:out value="${pr.id}"/> - <c:out value="${pr.number}"/></h3>
            </c:forEach>
        </div>


        <%@include file="common/footer.jsp" %>
