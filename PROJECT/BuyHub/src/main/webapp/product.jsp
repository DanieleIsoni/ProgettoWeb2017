<%--
    Document   : product
    Created on : 10-lug-2017, 09.56.05
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="product" class="it.unitn.buyhub.dao.entities.Product" scope="request"/>
<%@taglib prefix="gallery" uri="/WEB-INF/tld/gallery.tld"%>
<%@taglib prefix="pr" uri="/WEB-INF/tld/product.tld"%>


<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>

        <title>${product.name} - BuyHub</title>
    </head>
    <body >
        <%@include file="common/navbar.jsp" %>

    <div class="container header">
        <div class="row">
           <div class="col-md-12 product_category">
                <pr:category></pr:category>
            </div>

            </div>

            <div class="row">
                <div class="col-md-4 g">
                <gallery:Gallery></gallery:Gallery>
                </div>


                <div class="col-md-6">
                    <div class="product_title">
                    ${product.name}
                </div>
                <pr:ReviewStars count="${reviewsCount}" value="${globalValue}" label="true"></pr:ReviewStars>
                    <div class="product_description">
                    ${product.description}
                </div>
            </div>

            <div class="col-md-2">

                <div class="product_price">
                    <fmt:formatNumber type="currency" maxFractionDigits="2" minFractionDigits="2" value="${product.price}"  currencyCode="EUR"/>
                </div>

                <div class="product_shopname">
                    <c:choose>
                        <c:when test="${empty product.shop.website}">
                            ${product.shop.name}
                        </c:when>
                        <c:otherwise>
                            <a href="${product.shop.website}"> ${product.shop.name}</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="product_shop_description">
                    ${product.shop.description}
                </div>
                <div class="product_shop_owner">
                    ${product.shop.owner.firstName} ${product.shop.owner.lastName}
                </div>

            </div>


        </div>
        <div class="row reviews">
            <div class="row reviews_header">
                <fmt:message  key="customer_reviews"/>
            </div>

            <c:if test="${empty reviews}">
                <div>
                     <fmt:message  key="no_review"/>
                </div>
            </c:if>

            <c:forEach items="${reviews}" var="element">

                <div class="row review">

                    <div class="col-md-3">
                        <div class="review_img_box">
                            <img src="<c:url value="images/noimage.png"/>" class="img-rounded img-responsive">
                        </div>
                        <div class="review-block-name"><a href="#"><c:out value="${element.creator.firstName}"/></a></div>
                        <div class="review-block-date"><fmt:formatDate type = "date" dateStyle = "long" value = "${element.dateCreation}" />
                            <br/><time class="timeago" datetime="<fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm:ss'Z'"  value="${element.dateCreation}" />"></time>
                        </div>
                    </div>
                    <div class="col-md-9">
                        <pr:ReviewStars value="${element.globalValue}"></pr:ReviewStars>
                        <div class="review-block-title"><a href="#rev${element.id}" id="#rev${element.id}">
                                <c:out value="${element.title}"/>
                            </a>
                        </div>
                                <div class="review-block-description"><c:out value="${element.description}"/>
                                <br/>   
                                <button class="btn" data-toggle="collapse" data-target="#collapsedRev${element.id}"><fmt:message key="show_more"/></button>

                                <div id="collapsedRev${element.id}" class="collapse">

                                    <div class="detailed_review_header">
                                        <fmt:message key="quality"/>: <pr:ReviewStars value="${element.quality}"/>
                                        <fmt:message key="service"/>: <pr:ReviewStars value="${element.service}"/>
                                        <fmt:message key="value_for_money"/>: <pr:ReviewStars value="${element.valueForMoney}"/>


                                    </div>

                                </div>
                                </div>

                    </div>
                </div>
                 <hr/>    
            </c:forEach>       
        </div>
                <pr:ShopMap/>
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
   
