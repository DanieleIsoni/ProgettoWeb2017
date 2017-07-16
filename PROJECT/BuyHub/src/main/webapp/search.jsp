<%-- 
    Document   : search
    Created on : 16-lug-2017, 14.45.20
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="common/navbar.jsp" %>

        <title><fmt:message key="search_title"/></title>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-3 well center">
                    <!-- Filters -->
                    <h2>Filters</h2>
                    <br>
                    <h4>Price</h4>
                    <input type="text" class="range" prefix="€" value="" name="range" data-from="0" data-to="500" data-prefix="€" data-min="0" data-max="1000" />
                    <br>
                    <h4>Customer Review</h4>
                    <input type="text" class="single-range" value="" name="range" data-from="3" data-min="0" data-max="5" />

                </div>
                <div class="col-xs-7">
                    <!-- Product list -->

                </div>
            </div>
        </div>
    </body>

</html>
