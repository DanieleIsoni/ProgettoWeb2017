<%-- 
    Document   : editShop
    Created on : Nov 6, 2017, 12:03:47 PM
    Author     : Daniso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
         <%@include file="../common/header.jsp" %>
        <title><fmt:message key="editShop_title"/> - BuyHub</title>
    </head>
    <body>
        <div class="text-center login">
            <%@include file="../common/navbar.jsp" %>
            <img src="../images/icon.png" alt="BuyHub logo" height="42" width="42">
            <h3><fmt:message key="editShop_desc"/></h3>
            <br>
            <div class="panel panel-default panel-footer">
                <form method="POST" id="editShop-form" action="<c:url value="/EditShopServlet" />">
                    <div class="form-group">
                        <label for="shopName"><fmt:message key="shop_name"/>:</label>
                        <input type="text" name="shopName" class="form-control" id="shopName" value="${myshop.name}" >
                    </div>
                    <div class="form-group">
                        <label for="website"><fmt:message key="website"/>:</label>
                        <input type="url" name="website" class="form-control" id="website" value="${myshop.website}">
                    </div>
                    <div class="form-group">
                        <label for="shipment"><fmt:message key="shipment_mode"/>:</label>
                        <input type="text" name="shipment" class="form-control" id="shipment" value="${myshop.shipment}">
                    </div>

                    <div class="form-group">
                        <label for="description"><fmt:message key="description"/>:</label>
                        <textarea name="description" class="form-control" id="description" >${myshop.description}</textarea>
                    </div>

                    <button type="submit" class="btn btn-success"><fmt:message key="edit"/></button>
                </form>

            </div>
        </div>

        <%@include file="../common/footer.jsp" %>