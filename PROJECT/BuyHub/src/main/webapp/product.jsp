<%--
    Document   : product
    Created on : 10-lug-2017, 09.56.05
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="product" class="it.unitn.buyhub.dao.entities.Product" scope="request"/>
<%@taglib prefix="gallery" uri="/WEB-INF/tld/gallery.tld"%>
<%@taglib prefix="pr" uri="/WEB-INF/tld/product.tld"%>
<%@taglib prefix="map" uri="/WEB-INF/tld/map.tld"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>



<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>

        <title>${product.name} - BuyHub</title>
    </head>
    <body >
        <%@include file="common/navbar.jsp" %>


        <div class="container header">
            <err:ErrorMessage page="product"/>
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
                    <%--<pr:ReviewStars count="${reviewsCount}" value="${globalValue}" label="true"></pr:ReviewStars>--%>
                    <input type="hidden" class="rating" value="${product.avgReview}" data-readonly/> ${product.reviewCount} <fmt:message key="customer_reviews"/>
                    <br/>
                    <br/>
                    <div class="product_description">
                        ${product.description}
                        <br><br>
                        <form action="addtocart" method="GET">
                            <div class="input-group" id="cart-quantity"> 
                                <input name="count" class="form-control" placeholder="<fmt:message key="number_of_product"/>"> 
                                <span class="input-group-btn"> 
                                    <input type="hidden" name="id" value="${product.id}" />
                                    <input type="submit" class="btn btn-success" value="<fmt:message key="buy" />"/>
                                </span>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="col-md-2">

                    <div class="product_price">
                        <fmt:formatNumber type="currency" maxFractionDigits="2" minFractionDigits="2" value="${product.price}"  currencyCode="EUR"/>
                    </div>

                    <div class="product_shopname">

                        <a href="<c:url value="shop?id=${product.shop.id}"/>"> ${product.shop.name}</a>

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
                    <c:if test="${element.creator.id == authenticatedUser.id}">
                        ADD X button
                    </c:if>

                    <div class="row review">

                        <div class="col-md-3">
                            <div class="review_img_box">
                                <img src="<c:url value="${element.creator.avatar}"/>" class="img-rounded img-responsive">
                            </div>
                            <div class="review-block-name"><a href="<c:url value="user/?id=${element.creator.id}"/>"><c:out value="${element.creator.firstName}"/></a></div>
                            <div class="review-block-date"><fmt:formatDate type = "date" dateStyle = "long" value = "${element.dateCreation}" />
                                <br/><time class="timeago" datetime="<fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm:ss'Z'"  value="${element.dateCreation}" />"></time>
                            </div>
                        </div>
                        <div class="col-md-9">
                            <%--<pr:ReviewStars value="${element.globalValue}"></pr:ReviewStars>--%>
                            <input type="hidden" class="rating" value="${element.globalValue}}" data-readonly/>
                            <div class="review-block-title"><a href="#rev${element.id}" id="rev${element.id}">
                                    <c:out value="${element.title}"/>
                                </a>
                            </div>
                            <div class="review-block-description"><c:out value="${element.description}"/>
                                <br/>   
                                <button class="btn" data-toggle="collapse" data-target="#collapsedRev${element.id}"><fmt:message key="show_more"/></button>

                                <div id="collapsedRev${element.id}" class="collapse">

                                    <div class="detailed_review_header">
                                        <%--
                                        <fmt:message key="quality"/>: <pr:ReviewStars value="${element.quality}"/>
                                        <fmt:message key="service"/>: <pr:ReviewStars value="${element.service}"/>
                                        <fmt:message key="value_for_money"/>: <pr:ReviewStars value="${element.valueForMoney}"/>
                                        --%>
                                        <fmt:message key="quality"/>: <input type="hidden" class="rating" value="${element.quality}" data-readonly/>
                                        <br/>
                                        <fmt:message key="service"/>: <input type="hidden" class="rating" value="${element.service}" data-readonly/>
                                        <br/>
                                        <fmt:message key="value_for_money"/>: <input type="hidden" class="rating" value="${element.valueForMoney}}" data-readonly/>



                                    </div>

                                </div>
                            </div>

                        </div>
                    </div>
                    <hr/>    
                </c:forEach>   

                <!--
                Form for insert a review
                -->
                <c:if test = "${authenticatedUser != null}">
                    <form action="addreview">
                        <h4>  <fmt:message key="add_review"/> </h4>  
                        <div class="row review">

                            <div class="col-md-3">
                                <div class="review_img_box">
                                    <img src="${authenticatedUser.avatar}" class="img-rounded img-responsive">
                                </div>
                                <div class="review-block-name"></div>
                                <div class="review-block-date">
                                    <br/>
                                </div>
                            </div>
                            <div class="col-md-3">

                                <div class="detailed_review_header">
                                    <fmt:message key="rev_total"/> <input type="hidden" value="1" name="total" class="rating" />
                                    <br/>
                                    <fmt:message key="quality"/>: <input type="hidden" value="1" name="quality" class="rating" />
                                    <br/>
                                    <fmt:message key="service"/>: <input type="hidden" value="1" name="service" class="rating"  />
                                    <br/>
                                    <fmt:message key="value_for_money"/>: <input type="hidden" value="1" name="money" class="rating"  />
                                    <input type="hidden" name="prod_id" value="${product.id}"/>



                                </div>

                            </div>
                            <div class="col-md-6">
                                <input name="title" placeholder="<fmt:message key="review_title"/>" class="form-control"/>
                                <textarea name="description" placeholder="<fmt:message key="review_description"/>" class="form-control" id="description"></textarea>
                            </div>
                        </div>
                        <div class="center">
                            <input type="submit" value=" <fmt:message key="add_review"/>" class="btn btn-success" />
                        </div>
                    </form>
                </c:if>
                <!-- Fine form -->
            </div>
            <map:ShopMap/>
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

