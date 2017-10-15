<%--
    Document   : users
    Created on : 14-ott-2017, 15.04.02
    Author     : massimo
--%>

<!DOCTYPE html>

<html>
    <head>
        <%@include file="/common/header.jsp" %>
        <%@page import="it.unitn.buyhub.dao.entities.User"%>
        <jsp:useBean id="users" type="java.util.List<User>" scope="request"></jsp:useBean>
        <title><fmt:message key="userspage_title"/> - BuyHub</title>
        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
    </head>
<body>
    <%@include file="/common/navbar.jsp" %>

    <div class="container">

      <div class="col">
        <div class="row">
              <h2><b><fmt:message key="userspage_title"/></b></h2>
        </div>
        <div class="row">
        <table class="table table-striped table-bordered" id="users">
          <thead>
            <td> ID</td>
            <td> <fmt:message key="username"/></td>
            <td> <fmt:message key="full_name"/></td>
            <td> <fmt:message key="capability"/></td>
            <td> <fmt:message key="actions"/></td>


          </thead>

         <c:forEach items="${users}" var="user">
            <tr>
              <td>${user.id}</td>
              <td>${user.username}</td>
              <td>${user.firstName} ${user.lastName} </td>
              <td>
                  <fmt:message key="capability_${user.capability}"/>
              </td>
              <td>
                   <a href="<c:url value='EditUser?id=${user.id}'/>" role="button" class="btn_1 but btn btn-info">
                              <fmt:message key="edit"/>
                   </a>
                   
                   
              </td>


            </tr>

         </c:forEach>
        </table>
      </div>
     </div>
   </div>


     <script>

     /*Inizializzazione tabella*/
     $(document).ready(function() {
          $('#users').DataTable( {
        "language": {

               /*Datatable localization*/
               <fmt:message key="datatable_language"/>

        }
    } );
      } );


     </script>


<%@include file="/common/footer.jsp" %>
