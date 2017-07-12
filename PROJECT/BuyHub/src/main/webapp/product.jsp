<%--
    Document   : product
    Created on : 10-lug-2017, 09.56.05
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="product" class="it.unitn.buyhub.dao.entities.Product" scope="request"/>
<!--jsp:useBean id="reviews" class="java.util.List<it.unitn.buyhub.dao.entities.Review>" scope="request"/-->
<%@taglib prefix="gallery" uri="/WEB-INF/tld/gallery.tld"%>
<%@taglib prefix="pr" uri="/WEB-INF/tld/product.tld"%>

<%@include file="common/navbar.jsp" %>
  <div class="container header">
      <div class="row">
          
          <div class="col-md-1"></div>
          <div class="col-md-4">
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
                        
                        Recensioni degli utenti
                    </div>
                    <c:forEach begin="1" end="10">
                        
                        <div class="row review">

                            <div class="col-sm-3">
                                <div class="review_img_box">
                                    <img src="<c:url value="images/noimage.png"/>" class="img-rounded img-responsive">
                                </div>
                                <div class="review-block-name"><a href="#">Autore</a></div>
                                <div class="review-block-date">January 29, 2016<br/>1 day ago</div>
                            </div>
                            <div class="col-sm-9">
                                    <pr:ReviewStars value="3.2"></pr:ReviewStars>
                                    <div class="review-block-title">TITOLO</div>
                                    <div class="review-block-description">Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione Descrizione </div>
                            </div>
                            <hr/>	
                        </div>
                                    
                    </c:forEach>

    </div>


   </div>

   <script src="http://i-like-robots.github.io/EasyZoom/dist/easyzoom.js"></script>
   <script>
       	var $easyzoom = $('.easyzoom').easyZoom();

		// Setup thumbnails example
		var api1 = $easyzoom.filter('.easyzoom--with-thumbnails').data('easyZoom');

		$('.thumbnails').on('click', 'a', function(e) {
			var $this = $(this);

			e.preventDefault();

			// Use EasyZoom's `swap` method
			api1.swap($this.data('standard'), $this.attr('href'));
		});
    </script>