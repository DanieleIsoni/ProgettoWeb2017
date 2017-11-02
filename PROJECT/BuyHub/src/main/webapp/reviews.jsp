<%-- 
    Document   : cart
    Created on : 8-lug-2017, 20.55.51
    Author     : matteo
--%>

<!DOCTYPE html>
<%@taglib prefix="reviews" uri="/WEB-INF/tld/reviews.tld" %>
<html>
    <head>
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="reviews_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="common/navbar.jsp" %>

        <div class="container">
            <h2><b><fmt:message key="reviews_title"/></b></h2>
            <br>
            <reviews:ReviewsList id="${param.id}"/>
            
            

        </div>



        <%@include file="common/footer.jsp" %>
