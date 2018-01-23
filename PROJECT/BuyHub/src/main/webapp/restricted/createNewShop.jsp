<%-- 
    Document   : createNewShop
    Created on : Oct 6, 2017, 5:31:21 PM
    Author     : Daniele Isoni
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <%@include file="../common/header.jsp" %>
        <title><fmt:message key="createNewShop_title"/> - BuyHub</title>
    </head>
    <body>
        <div class="text-center login">
            <%@include file="../common/navbar.jsp" %>
            <img src="../images/icon.png" alt="BuyHub logo" height="42" width="42">
            <h3><fmt:message key="createNewShop_desc"/></h3>
            <br>
            <div class="panel panel-default panel-footer">
                <form method="POST" id="newShop-form" action="<c:url value="/CreateNewShopServlet" />">
                    <div class="form-group">
                        <label for="shopName"><fmt:message key="shop_name"/>*:</label>
                        <input type="text" name="shopName" class="form-control" id="shopName" required >
                    </div>
                    <div class="form-group">
                        <label for="website"><fmt:message key="website"/>*:</label>
                        <input type="url" name="website" class="form-control" id="website" required>
                    </div>
                    <div class="form-group">
                        <label for="shipment"><fmt:message key="shipment_mode"/>:</label>
                        <input type="text" name="shipment" class="form-control" id="shipment" >
                    </div>

                    <div class="form-group">
                        <label for="shipment_costs"><fmt:message key="shipment_costs"/>*:</label>
                        <input type="number" value="0.00" min="0.00" step="0.01" placeholder="e.g.: 25.99" name="shipment_costs" class="form-control" id="shipment_costs" >
                    </div>

                    <div class="form-group">
                        <label for="description"><fmt:message key="description"/>*:</label>
                        <textarea name="description" class="form-control" id="description" required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="autocomplete_address"><fmt:message key="autocomplete_address"/>:</label>
                        <input type="text" name="autocomplete_address" class="form-control" id="autocomplete_address" placeholder="" onFocus="geolocate()" >
                    </div>

                    <input type="hidden" name="latitude" id="latitude">
                    <input type="hidden" name="longitude" id="longitude">

                    <div class="form-group">
                        <label for="opening_hours"><fmt:message key="opening_hours"/></label>
                        <input type="text" name="opening_hours" class="form-control" id="opening_hours">
                    </div>

                    <button type="submit" class="btn btn-success"><fmt:message key="createshop"/></button>
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
