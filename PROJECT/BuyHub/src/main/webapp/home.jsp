<%-- 
    Document   : home
    Created on : 3-lug-2017, 18.07.05
    Author     : matteo
--%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="home_title"/></title>
    </head>
    <body>
         <%@include file="common/navbar.jsp" %>
       
       
        <div class="container header">
            <div id="myCarousel" class="carousel slide" data-ride="carousel">


                <!-- Wrapper for slides -->
                <div class="carousel-inner">

                    <div class="item active">
                        <img src="<c:url value="/images/carousel2.jpg" />" alt="Los Angeles" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>

                    <div class="item">
                        <img src="<c:url value="/images/carousel1.jpg" />" alt="Chicago" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>

                    <div class="item">
                        <img src="<c:url value="/images/carousel3.jpg" />" alt="New York" style="width:100%;">
                        <div class="carousel-caption">
                        </div>
                    </div>

                </div>

                <!-- Left and right controls -->
                <a class="left carousel-control" href="#myCarousel" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#myCarousel" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
        </div>

         <%@include file="common/footer.jsp" %>
