<%-- 
    Document   : navbar
    Created on : 1-lug-2017, 15.39.13
    Author     : matteo
--%>

<%@page import="it.unitn.buyhub.dao.entities.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="nv" uri="/WEB-INF/tld/navbar.tld"%>
<!DOCTYPE html>


<html>
    <%-- Include the bootstrap js, css and jquery--%>
    <%@include file="header.jsp" %>

    <body>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">
                        <img height="20"  alt="Brand" src="<c:url value="/images/icon.png" />">
                    </a>
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
                    <ul class="nav navbar-nav navbar-right" id="language_dropdown">
                        <li class="dropdown" onchange="submit()">
                            <a href="#" class="dropdown-toggle uppercase" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><div class="navbar-header two_chars">${language}</div><span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="<nv:languagelink lang="it"/>">IT</a></li>
                                <li><a href="<nv:languagelink lang="en"/>">EN</a></li>


                            </ul>
                        </li>

                    </ul>

                    <nv:userInformation />

                    <%--
                    <div class="input-group search_bar">
                       <select id="category_select" name="category" class="form-control">
                           <option value="-1" selected="selected"><fmt:message key="all_categories"/></option>
                            <nv:CategoriesPrinter  style="select"/>
                        </select>
                        
                        <!--this is ainvisible divider-->
                        <span class="input-group-addon" style="width:0px; padding-left:0px; padding-right:0px; border:none;"></span>
                        
                      <input type="text" name="search_text" class="form-control" aria-label="..."  placeholder="<fmt:message key="search"/>"> 
                      <span class="input-group-btn">
                        <button class="btn btn-default search_btn" type="button"><span class="glyphicon glyphicon-search"></span></button>
                      </span>
                    </div><!-- /input-group -->
                    --%>

                    <form  class="navbar-form" id="search" role="search">
                        <div class="input-group">
                            <div class="input-group-btn search-panel">
                                <select id="category_select" name="category" class="btn btn-default dropdown-toggle search-btn">
                                    <option value="-1" selected="selected"><fmt:message key="all_categories"/></option>
                                    <nv:CategoriesPrinter  style="select"/>
                                </select>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="#contains">Contains</a></li>
                                    <li><a href="#its_equal">It's equal</a></li>
                                    <li><a href="#greather_than">Greather than &gt;</a></li>
                                    <li><a href="#less_than">Less than &lt; </a></li>
                                    <li class="divider"></li>
                                    <li><a href="#all">Anything</a></li>
                                </ul>
                            </div>
                            <input type="hidden" name="search_param" value="greather_than" id="search_param">         
                            <input type="text" class="form-control" name="x" placeholder="Search term...">
                            <span class="input-group-btn">
                                <button class="btn btn-default search-btn" type="button"><span class="glyphicon glyphicon-search"></span></button>
                            </span>
                        </div>
                    </form>
                </div>
                <!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>

</html>
