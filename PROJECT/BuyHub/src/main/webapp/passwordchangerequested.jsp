<%-- 
    Document   : signedUp
    Created on : 18-ott-2017, 22.35.28
    Author     : massimo
--%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="password_requested_title"/></title>
    </head>
    <body>
         <%@include file="common/navbar.jsp" %>
       
       
        <div class="container header">
            
            <div class="row">
                <div class="col-md-2">
                    <div class="col-md-2">
                    <img class="success_image"src="images/success.jpg"/>
                </div>
                </div>
                <div class="col-md-10">
                    <h2>
                    <fmt:message key="password_requested_title"/>
                    </h2>
                    <br/>
                    <h4>
                    <fmt:message key="password_requested_desc"/>
                    </h4>
                </div>
            </div>
        </div>

         <%@include file="common/footer.jsp" %>
