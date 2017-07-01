<%-- 
    Document   : navbar
    Created on : 1-lug-2017, 15.39.13
    Author     : matteo
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <%-- Include the bootstrap js, css and jquery--%>
    <jsp:include page="include.jsp" />

    <body>
        Test lingua: <fmt:message key="welcome"/>
    </body>
</html>
