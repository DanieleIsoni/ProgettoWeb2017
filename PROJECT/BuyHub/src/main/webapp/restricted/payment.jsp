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
                            <label for="orderId"><fmt:message key="order_id"/>:</label>
                            <input type="text" class="form-control" value="${order.id}" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="shopName"><fmt:message key="shop_name"/>:</label>
                            <input type="text" class="form-control" value="${order.shop.name}" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="total"><fmt:message key="rev_total"/>:</label>
                            <input type="text" class="form-control" value="${requestScope.total}" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="shipment"><fmt:message key="shipment_mode_singular"/>:</label>
                            <input type="text" class="form-control" value="${order.shipment}" readonly="readonly">
                        </div>

                        <c:if test="${requestScope.shipmentType eq -1}" >

                            <div class="form-group">
                                <label for="address"><fmt:message key="address"/>*</label>
                                <textarea name="address" required="required" class="form-control" id="address" required></textarea>
                            </div>
                        </c:if>
                        
                        <div class="form-group">
                            <label for="card_owner"><fmt:message key="card_owner"/>:*</label>
                            <input type="text" maxlength="50" autocomplete="off" name="card_owner" class="form-control" required>
                        </div>
                            
                        <div class="form-group">
                            <label for="card_number"><fmt:message key="card_number"/>:*</label>
                            <input type="number" maxlength="16" autocomplete="off" name="card_number" class="form-control" required>
                        </div>
                            
                        <div class="form-group">
                            <label for="exp-date"><fmt:message key="exp-date"/>:*</label>
                            <select name="month" class="btn btn-default dropdown-toggle" id="month-exp-date">
                                <option value="0" selected>01</option>
                                <option value="1">02</option>
                                <option value="2">03</option>
                                <option value="3">04</option>
                                <option value="4">05</option>
                                <option value="5">06</option>
                                <option value="6">07</option>
                                <option value="7">08</option>
                                <option value="8">09</option>
                                <option value="9">10</option>
                                <option value="10">11</option>
                                <option value="11">12</option>
                            </select>
                            <select name="year" class="btn btn-default dropdown-toggle" id="year-exp-date">
                                <option value="0" selected>2018</option>
                                <option value="1">2019</option>
                                <option value="2">2020</option>
                                <option value="3">2021</option>
                                <option value="4">2022</option>
                                <option value="5">2023</option>
                                <option value="6">2024</option>
                                <option value="7">2025</option>
                                <option value="8">2026</option>
                                <option value="9">2027</option>
                                <option value="10">2028</option>
                            </select>
                        </div>
                            
                        <div class="form-group">
                            <label for="card_number"><fmt:message key="security_code"/>:*</label>
                            <input type="number" minlength="3" maxlength="5" autocomplete="off" name="card_number" class="form-control" required>
                        </div>

                        <div class="item_required"><fmt:message key="demo_pay_msg"/></div>
                        <br>
                        <div class="item_required"><fmt:message key="item_required"/></div>
                        <input type="hidden" name="orderid" value="${order.id}"/>
                        <button type="submit" class="btn btn-success"><fmt:message key="pay"/></button>
                        

                    </form>
                    <br>
                </div>
            </div>
        </div>
    </body>
</html>
