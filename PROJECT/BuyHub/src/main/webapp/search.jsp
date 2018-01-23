<%--
    Document   : search
    Created on : 16-lug-2017, 14.45.20
    Author     : Matteo Battilana
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="common/header.jsp" %>

        <title><fmt:message key="search_title"/> - BuyHub</title>
    </head>
    <body>

        <%@include file="common/navbar.jsp" %>
        <div class="container">
            <div class="row">
                <div class="col-xs-3 well center">
                    <!-- Filters -->
                    <h2><fmt:message key="filter"/></h2>
                    <br>
                    <h4><fmt:message key="price"/></h4>
                    <input type="text" id="price" class="range" prefix="€" value="" name="range" data-from="0" data-to="1000" data-prefix="€" data-min="0" data-max="1000" />
                    <br>
                    <h4><fmt:message key="avgreview"/></h4>
                    <!--<input type="text" id="minRev" class="single-range" value="0" name="range" data-from="0" data-min="0" data-max="5" />
                    -->                 <div dir="rtl">
                        <input type="hidden" id="minRev" class="rating" value="5" data-start="0" data-stop="5"/>
                    </div>
                    <div class="sorting_method  form-group">
                        <br/>
                        <h4><fmt:message key="orderby"/></h4>
                        <select class="form-control" id="orderby">
                            <option value="0" selected><fmt:message key="name"/> (A-Z)</option>
                            <option value="1"><fmt:message key="name"/> (Z-A)</option>
                            <option value="2"><fmt:message key="price_asc"/></option>
                            <option value="3"><fmt:message key="price_desc"/></option>
                            <option value="4"><fmt:message key="better_avg"/></option>
                            <option value="5"><fmt:message key="most_review"/></option>
                        </select>

                    </div>


                    <div id="locationPicker" class="form-group">
                        <h4><fmt:message key="locationPicker"/></h4>


                        <div class="form-group">
                            <label for="location"><fmt:message key="location"/></label>
                            <input type="text" name="location" class="form-control" placeholder="<fmt:message key="everywhere"/>"git id="location">
                        </div>

                        <div class="form-group">
                            <label for="distance"><fmt:message key="distance"/> (Km)</label>
                            <input type="number" step="50" min="0" name="distance" class="form-control" value="200" id="distance"></input>
                            <input name="lat" type="hidden" id="lat" value=0 />
                            <input name="lng" type="hidden"  id="lng" value=0 />
                        </div>


                    </div>


                </div>
                <div class="col-xs-9" id="products">
                    <!-- Product list -->
                </div>
            </div>
        </div>
        <script>
            $('#minRev').rating({
                extendSymbol: function (rate) {
                    $(this).tooltip({
                        container: 'body',
                        placement: 'bottom',
                        title: 5 - rate + " <fmt:message key="andmore"/>"

                    });
                }
            });
        </script>


        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjVcIi8WUN_UNmyn8JG1FncjBQUn6qk_g&libraries=places&callback=initAutocomplete"
        async defer></script>
        <script src="<c:url value="js/search.js" ></c:url>" ></script>

        <%@include file="common/footer.jsp" %>
