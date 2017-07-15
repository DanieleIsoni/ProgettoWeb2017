<%-- 
    Document   : error
    Created on : 3-lug-2017, 18.43.35
    Author     : matteo
--%>

<%@ page isErrorPage="true" %>  
<%@include file="header.jsp" %>


<div class="error-container" style="background-image: url('<c:url value="/images/errorPage/pattern-night.png" />');">
    <div id="cloud" style="background-image: url('<c:url value="/images/errorPage/pattern-cloud.png" />');"  />
    <img id="rocket" src="<c:url value="/images/errorPage/rocket.png" />" />
    <img id="e404" src="<c:url value="/images/errorPage/404.png" />" />

</div>