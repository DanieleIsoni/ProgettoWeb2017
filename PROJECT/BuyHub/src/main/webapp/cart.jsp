<%-- 
    Document   : cart
    Created on : 8-lug-2017, 20.55.51
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="userCart" scope="session" class="it.unitn.buyhub.dao.entities.Cart" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <c:forEach items="${userCart.products}" var="pr">
        ${pr.id}
    </c:forEach>
</body>
</html>
