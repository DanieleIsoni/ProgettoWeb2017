<%-- 
    Document   : addProduct
    Created on : Oct 23, 2017, 9:47:27 AM
    Author     : Daniso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>

        <%@include file="../common/header.jsp" %>
        <title><fmt:message key="addProduct_title"/> - BuyHub</title>
    </head>
    <body >
        <div class="text-center login">
            <%@include file="../common/navbar.jsp" %>
            <img src="../images/icon.png" alt="BuyHub logo" height="42" width="42">
            <h3><fmt:message key="addProduct_desc"/></h3>
            <br>
            <div class="panel panel-default panel-footer">
                <form method="POST" id="addProduct-form" action="<c:url value="/AddProductServlet" />">
                    <div class="form-group">
                        <label for="productName"><fmt:message key="product_name"/>*:</label>
                        <input type="text" name="productName" class="form-control" id="productName" >
                    </div>

                    <div class="form-group">
                        <label for="product_category"><fmt:message key="prod_categories"/>*:</label>
                        <div class="input-group-btn search-panel">
                            <select name="product_category" class="btn btn-default dropdown-toggle" id="product_category" >
                                <option value="-1" selected="selected"><fmt:message key="all_categories"/></option>
                                <nv:CategoriesPrinter  style="select"/>
                            </select> 
                        </div>     
                    </div>

                    <div class="form-group">
                        <label for="price"><fmt:message key="add_price"/>*:</label>
                        <input type="number" min="0.01" step="0.01" placeholder="e.g.: 25.99" name="price" class="form-control" id="price" >
                    </div>

                    <div class="form-group">
                        <label for="description"><fmt:message key="description"/>*:</label>
                        <textarea name="description" class="form-control" id="description" ></textarea>
                    </div>

                    <input type="hidden" name="shopId" id="shopId" value="${param.shopId}">

                    <button type="submit" class="btn btn-success"><fmt:message key="add_product"/></button>
                </form>
                <br>
                <div class="item_required"><fmt:message key="item_required"/></div>
            </div>
        </div>
        <%@include file="../common/footer.jsp" %>
