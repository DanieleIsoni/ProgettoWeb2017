<%--
    Document   : product
    Created on : 10-lug-2017, 09.56.05
    Author     : Massimo Girondi
--%>

<%@page import="it.unitn.buyhub.dao.entities.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="shop" class="it.unitn.buyhub.dao.entities.Shop" scope="request"/>
<%@taglib prefix="gallery" uri="/WEB-INF/tld/gallery.tld"%>

<%@taglib prefix="pr" uri="/WEB-INF/tld/product.tld"%>
<%@taglib prefix="map" uri="/WEB-INF/tld/map.tld"%>
<%@taglib prefix="shop" uri="/WEB-INF/tld/shop.tld" %>
<jsp:useBean id="products" type="java.util.List<Product>" scope="request"></jsp:useBean>


    <!DOCTYPE html>
    <html>
        <head>
        <%@include file="common/header.jsp" %>


        <link href="https://cdn.datatables.net/1.10.16/css/dataTables.bootstrap.min.css" type="text/css"/>
        <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js" ></script>
        <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>
        <title>${shop.name} - BuyHub</title>
    </head>
    <body >
        <%@include file="common/navbar.jsp" %>

        <div class="container header">

            <div class="row">
                <div class="col-md-4 g">
                    <gallery:Gallery></gallery:Gallery>
                    </div>


                    <div class="col-md-6">
                        <div class="shop_page_name">
                        ${shop.name}
                    </div>

                    <div class="shop_description">
                        ${shop.description}
                    </div>
                </div>

                <div class="col-md-2">

                    <div class="owner_info">
                        <fmt:message key="owner_info"/>
                    </div>
                    <div class="shop_page_owner">

                        ${shop.owner.firstName} ${shop.owner.lastName}

                    </div>

                </div>
            </div>
            <div class="row">

                <map:ShopMap/>
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

                    ${shop.shipment}
                </div>
            </div>

            <shop:ProductTableTagHandler shopId="${shop.id}" owner="false"/>

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
    <%@include file="common/footer.jsp" %>
