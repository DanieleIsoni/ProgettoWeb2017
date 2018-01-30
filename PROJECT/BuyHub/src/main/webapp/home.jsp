<%-- 
    Document   : home
    Created on : 3-lug-2017, 18.07.05
    Author     : Matteo Battilana
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="lastproduct" uri="/WEB-INF/tld/lastproduct.tld" %>
<%@taglib prefix="sliderImages" uri="/WEB-INF/tld/gallery.tld" %>
<!DOCTYPE html>
<html>
    <head>

        <%@include file="common/header.jsp" %>
        <title><fmt:message key="home_title"/></title>


    </head>
    <body>
        <%@include file="common/navbar.jsp" %>
        <div class="container header">
            <h1><fmt:message key="home_title"/></h1>



            <div id="myCarousel" class="carousel slide" data-ride="carousel">


                <!-- Wrapper for slides -->
                <div class="carousel-inner">

                    <sliderImages:HomepageSlider></sliderImages:HomepageSlider>


                    </div>

                    <!-- Left and right controls -->
                    <a class="left carousel-control slideshow_arrows" href="#myCarousel" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control slideshow_arrows" id="right_slide" href="#myCarousel" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
                <!--
                     <div>
                         <h4>Testo a caso</h4>
                         <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?</p>
                     </div> 
                -->
            </div>

        <lastproduct:LastProduct />
    </div>

    <%@include file="common/footer.jsp" %>
