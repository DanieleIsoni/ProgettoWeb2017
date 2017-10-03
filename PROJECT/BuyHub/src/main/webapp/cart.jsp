<%-- 
    Document   : cart
    Created on : 8-lug-2017, 20.55.51
    Author     : matteo
--%>

<!DOCTYPE html>
<%@taglib prefix="cart" uri="/WEB-INF/tld/cart.tld" %>
<html>
    <head>
        <%@include file="common/header.jsp" %>
        <title><fmt:message key="cart_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="common/navbar.jsp" %>

        <div class="container">
            <h2><b><fmt:message key="cart_title"/></b></h2>
            <br>
            <cart:CartList />
            

            <c:forEach items="${userCart.products}" var="pr">
               

                <div class="row search_item">
                    <div class="media"><div class="media-left"><div class="search_img_box"><a href="product?id=1"><img class="media-object img-rounded img-responsive" src="uploadedContent/7ae87bb4-0965-44bb-91f0-12b05c879da3.jpg" alt="Flessometro Stanley Tylon 3m"></a></div></div><div class="media-body"><h4 class="media-heading"><a href="product?id=1">Flessometro Stanley Tylon 3m</a></h4>
                            <div class="item_price">15.00<div class="item_rating"><span style="cursor: default;"><div class="rating-symbol" style="display: inline-block; position: relative;"><div class="rating-symbol-background glyphicon glyphicon-star-empty" style="visibility: hidden;"></div><div class="rating-symbol-foreground" style="display: inline-block; position: absolute; overflow: hidden; left: 0px; right: 0px; width: auto;"><span class="glyphicon glyphicon-star"></span></div></div><div class="rating-symbol" style="display: inline-block; position: relative;"><div class="rating-symbol-background glyphicon glyphicon-star-empty" style="visibility: hidden;"></div><div class="rating-symbol-foreground" style="display: inline-block; position: absolute; overflow: hidden; left: 0px; right: 0px; width: auto;"><span class="glyphicon glyphicon-star"></span></div></div><div class="rating-symbol" style="display: inline-block; position: relative;"><div class="rating-symbol-background glyphicon glyphicon-star-empty" style="visibility: hidden;"></div><div class="rating-symbol-foreground" style="display: inline-block; position: absolute; overflow: hidden; left: 0px; right: 0px; width: auto;"><span class="glyphicon glyphicon-star"></span></div></div><div class="rating-symbol" style="display: inline-block; position: relative;"><div class="rating-symbol-background glyphicon glyphicon-star-empty" style="visibility: visible;"></div><div class="rating-symbol-foreground" style="display: inline-block; position: absolute; overflow: hidden; left: 0px; right: 0px; width: 33.33%;"><span class="glyphicon glyphicon-star"></span></div></div><div class="rating-symbol" style="display: inline-block; position: relative;"><div class="rating-symbol-background glyphicon glyphicon-star-empty" style="visibility: visible;"></div><div class="rating-symbol-foreground" style="display: inline-block; position: absolute; overflow: hidden; left: 0px; right: 0px; width: 0px;"><span></span></div></div></span><input type="hidden" class="rating" value="3.3333" data-readonly=""></div><br>
                                <div class="shop_name"><a href="shop?id=1">Ferramenta del Corso</a></div>
                            </div></div></div><hr>
                </div>
                <h3><c:out value="${pr.id}"/> - <c:out value="${pr.number}"/></h3>
            </c:forEach>
        </div>


        <%@include file="common/footer.jsp" %>
