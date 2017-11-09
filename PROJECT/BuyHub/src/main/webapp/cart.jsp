<%-- 
    Document   : cart
    Created on : 8-lug-2017, 20.55.51
    Author     : matteo
--%>

<!DOCTYPE html>
<%@taglib prefix="cart" uri="/WEB-INF/tld/cart.tld" %>
<html>
    <head>
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="cart_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="common/navbar.jsp" %>

        <div class="container">
            <h2><b><fmt:message key="cart_title"/></b>    <button type="button" class="btn btn btn-danger" onclick="location.href = 'emptycart'"> <fmt:message key="empty_cart"/></button></h2>
                       
            <br>
            <cart:CartList />

        </div>



        <%@include file="common/footer.jsp" %>
