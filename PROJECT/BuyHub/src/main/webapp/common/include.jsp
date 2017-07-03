<%-- 
    Document   : include
    Created on : 1-lug-2017, 18.54.52
    Author     : matteo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Set language --%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" scope="session" />

<%-- Set Bundle language --%>
<fmt:setBundle basename="bundle.buyhubBundle" scope="application"/>



<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width , initial-scale=1" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <link href="<c:url value="/css/style.css" />" rel="stylesheet" type="text/css">    
    <script type="text/javascript" src="<c:url value="/js/script.js" />"></script>
    <link rel="icon" href="<c:url value="/images/icon.png" />">

</head>
