<!DOCTYPE html>
<html>
    <head>
        <%@page import="it.unitn.buyhub.dao.entities.Order"%>
        <jsp:useBean id="order" type="Order" scope="request"></jsp:useBean>
        <%@include file="/common/header.jsp" %>
        <title><fmt:message key="payment_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="/common/navbar.jsp" %>
        
        
        <c:set var="total" scope="page" value="${order.shipment_cost}"/>
        <c:forEach var="product" items="${order.products}">
            <c:set var="total" scope="page" value="${total + product.price}" />
            
        </c:forEach>
        
        <div class="row">
            <div class="col-md-4">
                
            <h1><fmt:message key="payment_title"/></h1>
                OrderID: ${order.id}<br/>
                ShopID:  ${order.shop.id}<br/>
                ShopID:  ${order.shop.name}<br/>
                Shipment: ${order.shipment}<br/>
                Total:  &euro; ${total}
                <br/>
                <%-- Need to implemnent some security token/checks -> This is just a DEMO of the payment page--%>
                <a class="btn btn-primary" href="./payed?orderid=${order.id}" role="button">Pay</a>
            </div>
        </div>
    </body>
</html>
