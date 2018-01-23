<%-- 
    Document   : myshop
    Created on : Sep 27, 2017, 2:28:48 PM
    Author     : Daniele
--%>

<%@page import="it.unitn.buyhub.dao.entities.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="gallery" uri="../WEB-INF/tld/gallery.tld"%>
<%@taglib prefix="pr" uri="../WEB-INF/tld/product.tld"%>
<%@taglib prefix="shop" uri="../WEB-INF/tld/shop.tld" %>

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
                        <fmt:message key="edit_shop_info" var="esi"/>
                        <a href="editShop.jsp" title="${esi}" id="edit_shop_btn"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
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

                    <c:if test="${empty myshop.shipment}">
                        <div>
                            <fmt:message  key="no_shipment"/>
                        </div>
                    </c:if>

                    ${myshop.shipment}
                </div>
            </div>

            <shop:ProductTableTagHandler shopId="${myshop.id}" owner="true"/>
            <shop:OrdersTagHanlder shop_id="${myshop.id}" />

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
            $('#orders_table').DataTable({
                "language": {

                    /*Datatable localization*/
        <fmt:message key="datatable_language"/>

                }
            });
        });
    </script>
    <%@include file="../common/footer.jsp" %>
