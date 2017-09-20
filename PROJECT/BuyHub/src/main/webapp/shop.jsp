<%--
    Document   : product
    Created on : 10-lug-2017, 09.56.05
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="shop" class="it.unitn.buyhub.dao.entities.Shop" scope="request"/>
<%@taglib prefix="gallery" uri="/WEB-INF/tld/gallery.tld"%>

<%@taglib prefix="pr" uri="/WEB-INF/tld/product.tld"%>


<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>

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
                        
                        <pr:ShopMap/>
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
 <%@include file="common/footer.jsp" %>
   
