$(function () {

    //se arrivo cercando dalla barra di ricerca carico i dati dalla url nel form
    $("#search_text").val(getUrlParameter("q").replace(/\+/g, " "));
    $("#search_category").val(getUrlParameter("c") ? getUrlParameter("c") : -1);

    $(".range").ionRangeSlider({
        hide_min_max: true,
        keyboard: true,
        type: 'double',
        step: 1,
        grid: true,
        onFinish: slide_change
    });

    /*
     $(".single-range").ionRangeSlider({
     hide_min_max: true,
     step: 1,
     onFinish: slide_change
     });*/
    $('#minRev').on('change', function () {
        cerca();
    });
    $('#orderby').on('change', function () {
        cerca();
    });


    $('#location').on('change', function () {
        cerca();
    });
    $('#distance').on('change', function () {
        cerca();
    });

    //invoco la prima ricerca
    cerca();

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    }


});

function cerca()
{
    cerca_pagina(1);
}

function cerca_pagina(p)
{
    var q = $("#search_text").val();
    var c = $("#search_category").val();
    var s = $("#orderby").val();
    var minRev = 5 - $("#minRev").val();
    var price = $("#price").val().split(";");
    var min = price[0];
    var max = price[1];
    var lat = $("#lat").val();
    var lng = $("#lng").val();
    var location = $("#location").val();
    var distance = $("#distance").val();
    var data;
    if (lat != 0 && lng != 0 && location != "" && distance > 0)
    {
        // ricerca geografica
        data = {"q": q,
            "c": c,
            "minRev": minRev,
            "min": min,
            "max": max,
            "s": s,
            "lat": lat,
            "lng": lng,
            "dist": distance,
            "p": p
        };
    } else {
        data = {"q": q,
            "c": c,
            "minRev": minRev,
            "min": min,
            "max": max,
            "s": s,
            "p": p
        };
    }
    $.ajax({
        method: "POST",
        url: "search",
        "data": data
    })
            .done(function (msg) {

                var products = $("#products");
                products.empty();
                for (var i in msg.products) {
                    //    for(var i=0;i<10;i++){
                    var p = msg.products[i];
                    //genero il prodotto
                    var s = "<div class='row search_item'>\n";
                    s += "<div class='media'>";
                    s += "<div class='media-left'>";
                    s += "<div class='search_img_box'><a href='product?id=" + p.id + "'><img class='media-object img-rounded img-responsive' src='" + p.mainPicture.path + "' alt='" + p.name + "'/></a></div>";
                    s += "</div>";
                    s += "<div class='media-body'>";
                    s += "<h4 class='media-heading'><a href='product?id=" + p.id + "'>" + p.name + "</a></h4>\n";
                    s += "<div class='item_price'>€ " + Number(p.price).toFixed(2);
                    +"</div><br/>\n";
                    if (p.avgReview != 0)
                        s += "<div class='item_rating' ><input type='hidden' class='rating' value=" + p.avgReview + " data-readonly/></div><br/>\n";

                    s += "<div class='shop_name'><a href='shop?id=" + p.shop.id + "'>" + p.shop.name + "</a></div>\n";

                    s += "</div></div></div><hr/>\n";

                    //costruisco la lista
                    products.append(s);



                }

                //stampo la lista delle pagine


                var s = "<div class='row pages'>\n";

                if (msg.pages < 7)
                {
                    for (var i = 1; i <= msg.pages; i++)
                        if (i != msg.p)
                            s += "<button type='button' class='btn btn-secondary' onclick=\"cerca_pagina(" + i + ")\">" + i + "</button>";
                        else
                            s += "<button type='button' class='btn btn-primary' disabled>" + i + "</button>";
                    products.append(s);
                } else
                {
                    var i = (msg.p - 3) > 1 ? (msg.p - 3) : 1;
                    i += 1;
                    var f = (msg.p + 3) > msg.pages ? msg.pages : (msg.p + 3);

                    console.log(i + "," + f)
                    //stampo il primo
                    if (1 != msg.p)
                        s += "<button type='button' class='btn btn-secondary' onclick=\"cerca_pagina(" + 1 + ")\">" + 1 + "</button>";
                    else
                        s += "<button type='button' class='btn btn-primary' disabled>" + 1 + "</button>";

                    if (i != 2)
                        s += "...";

                    for (; i < f; i++) {
                        if (i != msg.p)
                            s += "<button type='button' class='btn btn-secondary' onclick=\"cerca_pagina(" + i + ")\">" + i + "</button>";
                        else
                            s += "<button type='button' class='btn btn-primary' disabled>" + i + "</button>";
                    }
                    //non ho stampato il penultimo ma uno prima

                    //se non l'ho stampato
                    if (f - 1 < msg.pages)
                    {
                        if (f < msg.pages)
                            s += "...";

                        if (msg.pages != msg.p)
                            s += "<button type='button' class='btn btn-secondary' onclick=\"cerca_pagina(" + msg.pages + ")\">" + msg.pages + "</button>";
                        else
                            s += "<button type='button' class='btn btn-primary' disabled>" + msg.pages + "</button>";
                    }

                    products.append(s);
                    /*
                     //Modifico il range dello slider del prezzo
                     var inputRange = $('.range');
                     var attributes = {
                     "min": msg.min,
                     "max": msg.max
                     
                     };
                     inputRange.attr(attributes).rangeslider('update', true);
                     */
                }


                // products.append("\n<br/>\nPagina "+msg.p+" di "+msg.pages);
                $(".rating").rating();

            });



}

function slide_change(data)
{
    cerca();
}


/*
 Autocomplete the location field with Google Maps API
 */
var autocomplete;

function initAutocomplete() {
    // Create the autocomplete object, restricting the search to geographical
    // location types.
    autocomplete = new google.maps.places.Autocomplete(
            /** @type {!HTMLInputElement} */(document.getElementById('location')),
            {types: ['geocode']});
    autocomplete.addListener('place_changed', FillInAddress);
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

function FillInAddress()
{
    var place = autocomplete.getPlace();
    var lat = place.geometry.location.lat();
    var lng = place.geometry.location.lng();
    $('#lat').val(lat);
    $('#lng').val(lng);

}

function getLocation() {

}
function showPosition(position) {
    $("#location").val("\u2316 GPS");
    $('#lat').val(position.coords.latitude);
    $('#lng').val(position.coords.longitude);
    cerca();
}
