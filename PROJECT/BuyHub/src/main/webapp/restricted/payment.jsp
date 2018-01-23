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

        <div class="container">
            <div >

                <h1><fmt:message key="payment_title"/></h1>


                <div class="panel panel-default panel-footer">
                    <form method="POST" id="addProduct-form" action="./payed">
                        <div class="form-group">
                            <label for="productName">Order ID:</label>
                            <input type="text" class="form-control" value="${order.id}" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="productName">Shop Name:</label>
                            <input type="text" class="form-control" value="${order.shop.name}" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="productName">Total amount:</label>
                            <input type="text" class="form-control" value="${requestScope.total}" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="productName">Shipment:</label>
                            <input type="text" class="form-control" value="${order.shipment}" readonly="readonly">
                        </div>

                        <c:if test="${requestScope.shipmentType eq -1}" >

                            <div class="form-group">
                                <label for="address">Address*:</label>
                                <textarea name="address" required="required" class="form-control" id="address"></textarea>
                            </div>
                        </c:if>

                        <div class="item_required">This is just a demo, it should be replaced by PayPal or similar gateway.</div>
                        <input type="hidden" name="orderid" value="${order.id}"/>
                        <button type="submit" class="btn btn-success"><fmt:message key="pay"/></button>

                    </form>
                    <br>
                </div>









            </div>
        </div>
    </body>
</html>
