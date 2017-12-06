<!DOCTYPE html>
<html>
    <head>
        <%@page import="it.unitn.buyhub.dao.entities.Order"%>
        <jsp:useBean id="order" type="Order" scope="request"></jsp:useBean>
        <%@include file="/common/header.jsp" %>
        <title>Checkout - BuyHub</title>
        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
    </head>
    <body>
        <%@include file="/common/navbar.jsp" %>
        
        
        <c:set var="total" scope="page" value="${order.shipment_cost}"/>
        <c:forEach var="product" items="${order.products}">
            <c:set var="total" scope="page" value="${total + product.price}" />
            
        </c:forEach>
        
        <h1>Pagamento</h1>
            OrderID: ${order.id}<br/>
            ShopID:  ${order.shop.id}<br/>
            ShopID:  ${order.shop.name}<br/>
            Shipment: ${order.shipment}<br/>
            Total:  &euro; ${total}
    </body>
</html>
