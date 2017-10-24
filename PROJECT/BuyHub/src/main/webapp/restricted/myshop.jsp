<%-- 
    Document   : myshop
    Created on : Sep 27, 2017, 2:28:48 PM
    Author     : Daniele
--%>

<%@page import="it.unitn.buyhub.dao.entities.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="gallery" uri="../WEB-INF/tld/gallery.tld"%>
<%@taglib prefix="pr" uri="../WEB-INF/tld/product.tld"%>
<%@taglib prefix="map" uri="../WEB-INF/tld/map.tld"%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../common/header.jsp" %>
        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
        <title><fmt:message key="myshop_page"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="../common/navbar.jsp" %>
        <div class="container header">

            <div class="row">
                <div class="col-md-4 g">
                    <gallery:Gallery></gallery:Gallery>
                    </div>


                    <div class="col-md-6">
                        <div class="shop_page_name">
                        ${myshop.name}
                    </div>

                    <div class="shop_description">
                        ${myshop.description}
                    </div>
                </div>

                <div class="col-md-2">

                    <div class="owner_info">
                        <fmt:message key="owner_info"/>
                    </div>
                    <div class="shop_page_owner">

                        ${myshop.owner.firstName} ${myshop.owner.lastName}

                    </div>

                </div>
            </div>

            <hr>
            <div class="row">
                <div class="row shop_page_shipment_info">

                    <fmt:message key="shipment_mode"/>

                </div>
                <div class="row shop_page_shipment">

                    <c:if test="${empty shop.shipment}">
                        <div>
                            <fmt:message  key="no_shipment"/>
                        </div>
                    </c:if>

                    ${myshop.shipment}
                </div>
            </div>
            <div class="row">

                <div class="row shop_page_shipment_info">
                    <fmt:message key="all_products_shop"/>
                </div>

                <table class="table table-striped table-bordered" id="products_table">
                    <thead>

                    <td> <fmt:message key="name"/></td>
                    <td> <fmt:message key="category"/></td>
                    <td> <fmt:message key="price"/></td>
                    <td>
                        <fmt:message key="add_product" var="plus"/>
                        <a href="addProduct.jsp?shopId=${myshop.id}" title="${plus}" class="btn btn-primary a-btn-slide-text mybtn">
                            <span class="glyphicon myglyph glyphicon-plus" aria-hidden="true"></span>          
                        </a>
                    </td>

                    </thead>


                    <c:forEach items="${myproducts}" var="product">
                        <tr>
                            <td><a href="product?id=${product.id}"/>${product.name}</a></td>
                            <td><pr:category category="${product.category}"/> </td>

                            <td>
                                € <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${product.price}"/>
                            </td>
                            <td>
                                <fmt:message key="edit_product" var="edit"/>
                                <a href="#" title="${edit}" class="btn btn-primary a-btn-slide-text mybtn">
                                    <span class="glyphicon myglyph glyphicon-edit" aria-hidden="true"></span>          
                                </a>
                                <fmt:message key="delete_product" var="delete"/>
                                <a href="#" title="${delete}" class="btn btn-danger a-btn-slide-text mybtn">
                                    <span class="glyphicon myglyph glyphicon-remove" aria-hidden="true"></span>          
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

        </div>
    </div>
    <script src="http://i-like-robots.github.io/EasyZoom/dist/easyzoom.js"></script>
    <script>
        var $easyzoom = $('.easyzoom').easyZoom();

        // Setup thumbnails example
        var api1 = $easyzoom.filter('.easyzoom--with-thumbnails').data('easyZoom');

        $('.thumbnails').on('click', 'a', function (e) {
            var $this = $(this);

            e.preventDefault();

            // Use EasyZoom's `swap` method
            api1.swap($this.data('standard'), $this.attr('href'));
        });
    </script>

    <script>
        /*Inizializzazione tabella*/
        $(document).ready(function () {
            $('#products_table').DataTable({
                "language": {

                    /*Datatable localization*/
        <fmt:message key="datatable_language"/>

                }
            });
        });
    </script>
    <%@include file="../common/footer.jsp" %>
