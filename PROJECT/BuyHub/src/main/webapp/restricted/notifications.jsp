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
        <%@taglib prefix="notification" uri="../WEB-INF/tld/notification.tld" %>


        <title><fmt:message key="notification_title"/> - BuyHub</title>
        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>

    </head>
    <body >
        <%@include file="../common/navbar.jsp" %>
        <div class="container" >
            <div class="col">
                <div class="row">
                    <h2><b><fmt:message key="notification_title"/></b></h2>
                </div>
            </div>
            <div class="row">
                <table class="table table-striped table-bordered" id="shops">
                    <thead>
                    <td> <fmt:message key="notification_title"/></td>


                    </thead>
                    <notification:NotificationList />

                    <c:forEach items="${shops}" var="shop">
                        <tr>
                            <td>${shop.id}</td>
                            <td><a href="../../shop?id=${shop.id}"> ${shop.name}</td>
                            <td><a href="user?id=${shop.owner.id}"/>  ${shop.owner.firstName} ${shop.owner.lastName} (${shop.owner.username}) </td>
                            <td>
                                <c:choose>
                                    <c:when test="${shop.validity==0}">
                                        <a href="<c:url value='/restricted/admin/enableshop?id=${shop.id}&status=1'/>" role="button" class="btn_1 but btn btn-success">
                                            <fmt:message key="enable"/>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="<c:url value='EnableShop?id=${shop.id}&status=0'/>" role="button" class="btn_1 but btn btn-danger">
                                            <fmt:message key="disable"/>
                                        </a>
                                    </c:otherwise>    
                                </c:choose>
                            </td>


                        </tr>

                    </c:forEach>
                </table>
            </div>
        </div>

        
    </div>
    <script>

        /*Inizializzazione tabella*/
        $(document).ready(function () {
            $('#shops').DataTable({
                "language": {

                    /*Datatable localization*/
        <fmt:message key="datatable_language"/>

                }
            });
        });


    </script>

    <%@include file="../common/footer.jsp" %>