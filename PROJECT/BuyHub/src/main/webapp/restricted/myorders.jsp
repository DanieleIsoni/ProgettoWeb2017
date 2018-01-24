<%-- 
    Document   : myorders.jsp
    Created on : 13-dic-2017, 18.12.44
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../common/header.jsp" %>
        <%@page import="it.unitn.buyhub.dao.entities.Order"%>
        <jsp:useBean id="orders" type="java.util.List<Order>" scope="request"></jsp:useBean>
        <jsp:useBean id="totals" type="java.util.List<Double>" scope="request"></jsp:useBean>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

      
        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
        
        <title><fmt:message key="myorders_title"/> - BuyHub</title>
    </head>
    <body >
        <%@include file="../common/navbar.jsp" %>


    <div class="container">
        
        <div class="row product_title">
           
            <fmt:message key="myorders_title"/> 

            </div>
                     <div class="row">
        <table class="table table-striped table-bordered" id="users">
          <thead>
            <td> ID</td>
            <td> <fmt:message key="shop_name"/></td>
            <td> <fmt:message key="shipment_mode_singular"/></td>
            <td> <fmt:message key="cart_total"/></td>
            <td/> <fmt:message key="actions"/></td>
          </thead>

         <c:forEach var="i" begin="0" end="${fn:length(orders) -1}">
             <c:set var="order" value="${orders[i]}"/>
            <c:set var="total" value="${totals[i]}"/>
             <tr>
              <td>#${order.id}</td>
              <td>${order.shop.name}</td>
              <td>${order.shipment}</td>
              <td>&euro; <fmt:formatNumber minFractionDigits="2" maxFractionDigits="2">${total}</fmt:formatNumber></td>
              <td>
                   <a href="<c:url value="openticket"/>?id_order=${order.id}" role="button" class="btn_1 but btn btn-info"><fmt:message key="ticket"/>
                   </a>
              </td>


            </tr>

         </c:forEach>
        </table>
      </div>
    </div>
                    
           
    <script>
        /*Inizializzazione tabella*/
        $(document).ready(function () {
            $('#products_table').DataTable({
                "language": {

                    /*Datatable localization*/
        <fmt:message key="datatable_language"/>

                }
            });
        });
    </script>   
<%@include file="../common/footer.jsp" %>