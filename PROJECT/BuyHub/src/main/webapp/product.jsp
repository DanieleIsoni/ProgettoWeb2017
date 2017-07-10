
<%--
    Document   : product
    Created on : 10-lug-2017, 09.56.05
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="product" class="it.unitn.buyhub.dao.entities.Product" scope="session"/>

<%@include file="common/navbar.jsp" %>
  <div class="container header">
      <div class="row">
          <div class="col-md-4">
              <div class="easyzoom1">
                  
                <div class="easyzoom easyzoom--overlay easyzoom--with-thumbnails">
                <a href="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg">
                        <img src="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" alt="" width="100%" height="100%" />
                </a>
                </div>

                <ul class="thumbnails">
                        <li>
                                <a href="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" data-standard="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg">
                                        <img src="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" alt="" />
                                </a>
                        </li>
                        <li>
                                <a href="euploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" data-standard="euploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg">
                                        <img src="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" alt="" />
                                </a>
                        </li>
                        <li>
                                <a href="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpgg" data-standard="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg">
                                        <img src="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" alt="" />
                                </a>
                        </li>
                        <li>
                                <a href="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" data-standard="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg">
                                        <img src="uploadedContent/34e329d7-b874-4daa-b46b-2ad194da8e9c.jpg" alt="" />
                                </a>
                        </li>
                </ul>
                </div>
              
              
              
              
              
          </div>


            <div class="col-md-6">
              <div class="product_title">
                  ${product.name}
              </div>
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

    </div>


   </div>

   <script src="<c:url value="js/easyzoom.js"/>"></script>
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