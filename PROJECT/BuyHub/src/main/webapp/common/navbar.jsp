<%-- 
    Document   : navbar
    Created on : 1-lug-2017, 15.39.13
    Author     : matteo
--%>

<%@page import="it.unitn.buyhub.dao.entities.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="right-corner" uri="/WEB-INF/tld/navbar.tld"%>
<!DOCTYPE html>


<html>
    <%-- Include the bootstrap js, css and jquery--%>
    <%@include file="include.jsp" %>


    <body>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">BuyHub</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">

                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown" onchange="submit()">
                            <a href="#" class="dropdown-toggle uppercase" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><div class="navbar-header two_chars">${language}</div><span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="?language=it">IT</a></li>
                                <li><a href="?language=en">EN</a></li>
                            </ul>
                        </li>

                    </ul>

                    <right-corner:userInformation />

                    <form class="navbar-form navbar-right">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="<fmt:message key="search"/>">
                        </div>
                    </form>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
    </body>

</html>
