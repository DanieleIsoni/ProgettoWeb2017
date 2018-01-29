<%-- 
    Document   : addProduct
    Created on : Oct 23, 2017, 9:47:27 AM
    Author     : Daniele Isoni
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>

<!DOCTYPE html>
<html>
    <head>

        <%@include file="../common/header.jsp" %>
        <title><fmt:message key="addCoordinate_title"/> - BuyHub</title>
    </head>
    <body>
        
            <%@include file="../common/navbar.jsp" %>
        <div class="text-center login">
            <img src="../images/icon.png" alt="BuyHub logo" height="42" width="42">
            <h3><fmt:message key="addCoordinate_desc"/></h3>
            <br>
            <div class="panel panel-default panel-footer">
                <err:ErrorMessage page="addCoordinate"/>
                <form method="POST" id="addCoordinate-form" action="<c:url value="/AddCoordinateServlet" />">
                    <div class="form-group">
                        <label for="autocomplete_address"><fmt:message key="autocomplete_address"/>*:</label>
                        <input type="text" name="autocomplete_address" class="form-control" id="autocomplete_address" placeholder="" onFocus="geolocate()" >
                    </div>

                    <input type="hidden" name="latitude" id="latitude">
                    <input type="hidden" name="longitude" id="longitude">

                    <div class="form-group">
                        <label for="opening_hours"><fmt:message key="opening_hours"/></label>
                        <input type="text" name="opening_hours" class="form-control" id="opening_hours">
                    </div>

                    <input type="hidden" name="shopId" id="shopId" value="${param.shopId}">

                    <button type="submit" class="btn btn-success"><fmt:message key="add"/></button>
                </form>
                <br>
                <div class="item_required"><fmt:message key="item_required"/></div>
            </div>
        </div>
        <script>
            var autocomplete;

            function initAutocomplete() {
                // Create the autocomplete object, restricting the search to geographical
                // location types.
                autocomplete = new google.maps.places.Autocomplete(
                        /** @type {!HTMLInputElement} */(document.getElementById('autocomplete_address')),
                        {types: ['geocode']});
                autocomplete.addListener('place_changed', fillInAddress);
            }

            function geolocate() {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        var geolocation = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };
                        var circle = new google.maps.Circle({
                            center: geolocation,
                            radius: position.coords.accuracy
                        });
                        autocomplete.setBounds(circle.getBounds());
                    });
                }
            }

            function fillInAddress() {
                // Get the place details from the autocomplete object.
                var place = autocomplete.getPlace();
                var lat = place.geometry.location.lat(),
                        lng = place.geometry.location.lng();

                // Get each component of the address from the place details
                // and fill the corresponding field on the form.
                document.getElementById("latitude").value = lat;
                document.getElementById("longitude").value = lng;
            }
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjVcIi8WUN_UNmyn8JG1FncjBQUn6qk_g&libraries=places&callback=initAutocomplete"
        async defer></script>
        <%@include file="../common/footer.jsp" %>
