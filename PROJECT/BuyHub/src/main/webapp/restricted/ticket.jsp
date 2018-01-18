<%-- 
    Document   : ticket
    Created on : 18-gen-2018, 12.07.17
    Author     : matteo
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="message" uri="/WEB-INF/tld/message.tld" %>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../common/header.jsp" %>
        <title><fmt:message key="ticket_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="../common/navbar.jsp" %>

        <div class="container">
            <h2><b><fmt:message key="ticket_title"/> - ${param.id}</b></h2>
            <br>

            <div class="box">

                <message:MessageList />


               


            </div>
            <br><br>
            <form action="<c:url value="/restricted/sendmessage" />" method="POST">
                <div  class="input-group">
                    <input type="text" name="text" class="form-control">
                    <input type="hidden" name="ticket_id" value="${param.id}">
                    <span class="input-group-btn">
                        <input type="submit" class="btn btn-default"><fmt:message key="send"/></button>
                    </span>
                     
                </div>
            </form>

        </div>



        <%@include file="../common/footer.jsp" %>