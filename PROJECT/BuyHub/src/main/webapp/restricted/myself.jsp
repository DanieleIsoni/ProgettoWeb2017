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

        <title><fmt:message key="user_page"/> - BuyHub</title>
    </head>
    <body >
        <%@include file="../common/navbar.jsp" %>

        
    <div class="container">
        
        <div class="row">
            <div class="profile_img_box col-md-5">
                <img class=" img-rounded img-responsive" src="<c:url value="../${authenticatedUser.avatar}"/>" alt="">
            </div>
            <div class="col-md-5">
                  <div class="profile_name">${authenticatedUser.firstName} ${authenticatedUser.lastName}</div>
        <div class="profile_username">${authenticatedUser.username} </div>
        
            </div>
        </div>
            
      
        
        
    </div>
        
<%@include file="../common/footer.jsp" %>