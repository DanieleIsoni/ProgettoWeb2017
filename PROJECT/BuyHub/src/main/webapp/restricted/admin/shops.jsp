<%--
    Document   : shops
    Created on : 14-ott-2017, 15.04.02
    Author     : massimo
--%>

<!DOCTYPE html>

<html>
    <head>
        <%@include file="/common/header.jsp" %>
        <%@page import="it.unitn.buyhub.dao.entities.Shop"%>
        <jsp:useBean id="shops" type="java.util.List<Shop>" scope="request"></jsp:useBean>
        <title><fmt:message key="shopspage_title"/> - BuyHub</title>
        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
    </head>
<body>
    <%@include file="/common/navbar.jsp" %>

    <div class="container">

      <div class="col">
        <table class="table table-striped table-bordered" id="shops">
          <thead>
            <td> ID</td>
            <td> <fmt:message key="shop_name"/></td>
            <td> <fmt:message key="owner"/></td>
            <td> <fmt:message key="actions"/></td>


          </thead>

         <c:forEach items="${shops}" var="shop">
            <tr>
              <td>${shop.id}</td>
              <td><a href="../../shop?id=${shop.id}"> ${shop.name}</td>
              <td><a href="user?id=${shop.owner.id}"/>  ${shop.owner.firstName} ${shop.owner.lastName} (${shop.owner.username}) </td>
              <td>  </td>


            </tr>

         </c:forEach>
        </table>
      </div>
     </div>


     <script>

     /*Inizializzazione tabella*/
     $(document).ready(function() {
          $('#shops').DataTable( {
        "language": {

               /*Datatable localization*/
               <fmt:message key="datatable_language"/>

        }
    } );
      } );


     </script>


<%@include file="/common/footer.jsp" %>
