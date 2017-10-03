<%--
    Document   : myself
    Created on : 8-ago-2017, 15.58.18
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../common/header.jsp" %>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.4/css/fileinput-rtl.min.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.4/js/fileinput.min.js"></script>
        <title><fmt:message key="user_page"/> - BuyHub</title>
    </head>
    <body >
        <%@include file="../common/navbar.jsp" %>


    <div class="container">
        <div class="row">
            <div class="row">
                <div class="col-md-5">
                    <div class="profile_name">${authenticatedUser.firstName} ${authenticatedUser.lastName}</div>
                    <div class="profile_username">${authenticatedUser.username} </div>

                </div>
            </div>
            <div class="row ">
                <div class="link_box col-md-6 col-centered">
                    <ul class="list-unstyled ">
                        <li> <a href="modifyAccount.jsp" ><fmt:message key="modify_account"/></a> </li>
                        <c:choose>
                            <c:when test="${authenticatedUser.capability eq 2}">
                                <li><a href="myshop.jsp"><fmt:message key="myshop_page"/></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="#"><fmt:message key="create_shop"/></a></li>
                            </c:otherwise>
                        </c:choose>
                        <li> <a href="#"><fmt:message key="ask_refund"/></a> </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>


                
<%@include file="../common/footer.jsp" %>
