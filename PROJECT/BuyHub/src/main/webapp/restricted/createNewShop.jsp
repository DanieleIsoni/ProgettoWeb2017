<%-- 
    Document   : createNewShop
    Created on : Oct 6, 2017, 5:31:21 PM
    Author     : Daniso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
        <%@include file="../common/header.jsp" %>
        <title><fmt:message key="createNewShop_title"/> - BuyHub</title>
    </head>
    <body class="text-center login">
        
        <%@include file="../common/navbar.jsp" %>
        <img src="../images/icon.png" alt="BuyHub logo" height="42" width="42">
        <h3><fmt:message key="createNewShop_desc"/></h3>
        <br>
        <div class="panel panel-default panel-footer">
            <form method="POST" id="newShop-form" action="<c:url value="/CreateNewShopServlet" />">
                <div class="form-group">
                    <label for="shopName"><fmt:message key="shop_name"/>:</label>
                    <input type="text" name="shopName" class="form-control" id="shopName">
                </div>
                <div class="form-group">
                    <label for="website"><fmt:message key="website"/>:</label>
                    <input type="url" name="website" class="form-control" id="website">
                </div>
                <div class="form-group">
                    <label for="shipment"><fmt:message key="shipment_mode"/>:</label>
                    <input type="text" name="shipment" class="form-control" id="shipment">
                </div>

                <div class="form-group">
                    <label for="description"><fmt:message key="description"/>:</label>
                    <input type="text" name="description" class="form-control" id="description">
                </div>

                <button type="submit" class="btn btn-success"><fmt:message key="createshop"/></button>
            </form>
        </div>

        <%@include file="../common/footer.jsp" %>
