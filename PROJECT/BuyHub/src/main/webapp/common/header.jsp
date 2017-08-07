<%-- 
    Document   : include
    Created on : 1-lug-2017, 18.54.52
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- Set language --%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" scope="session" />

<%-- Set Bundle language --%>
<fmt:setBundle basename="bundle.buyhubBundle" scope="application"/>
<jsp:useBean id="userCart" scope="session" class="it.unitn.buyhub.dao.entities.Cart" />


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width , initial-scale=1" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="<c:url value="/css/style.css" />" rel="stylesheet" type="text/css">    
<link href="http://i-like-robots.github.io/EasyZoom/css/easyzoom.css" rel="stylesheet" type="text/css">    
<link href="<c:url value="/css/easyzoom_custom.css" />"  rel="stylesheet" type="text/css">    
<link rel="icon" href="<c:url value="/images/icon.png" />">
<script src="http://timeago.yarp.com/jquery.timeago.js" type="text/javascript"></script>

<!-- load the correct locale, based on the first two chars of language, extracted with EL function-->
<script src="<c:url value="/js/jquery.timeago.locales/jquery.timeago.${fn:substring(language,0,2)}.js"/>" type="text/javascript"></script>

<!-- ion.rangeSlider -->
<link rel="stylesheet" href="<c:url value="/css/ion.rangeSlider.css" />" />
<link rel="stylesheet" href="<c:url value="/css/ion.rangeSlider.skinFlat.css" />" />
<script type="text/javascript" src="<c:url value="/js/ion.rangeSlider.js" />"></script>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-3-typeahead/4.0.2/bootstrap3-typeahead.min.js"></script>


<script type="text/javascript" src="<c:url value="/js/script.js" />"></script>
