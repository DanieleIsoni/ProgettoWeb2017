/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('[data-toggle="popover"]').popover();

    $("time.timeago").timeago();

    $('#search_category').change(function () {
        $("#width-tmp-option").html($('#search_category option:selected').text());
        $(this).width($("#width-tmp-select").width());
    });


    $("#search_text").typeahead({
        source: function (query, process) {
            //Use this URL to avoid errors on subdirectories
            return $.getJSON('../../../../../BuyHub/autocomplete', {term: query, cat: $("#search_category").val()}, function (data) {
                console.log(data.res);

                data = data.res;

                return process(data);
            });
        },
        minLength: 3,
        items: 8,
        matcher: function (item) {
            // to display not matching items
            return true;
        }
    });





});

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

//Cart script, called when the number of item is modified. In this case the value in the bean is changed to te correct number;
//Once it's changed, you need to press the Recalculate button at the bottom of the page; It's possible to recaulculate the price at every modification
function changeItemNumber(id, value) {
    //modifycartitemnumber?id=33&count=12
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "modifycartitemnumber?id="+id+"&count="+value, true);
    xhttp.send();

}




function setCharAt(str,index,chr) {
    if(index > str.length-1) return str;
    return str.substr(0,index) + chr + str.substr(index+1);
}


function changeShipment(shopid)
{
    var total=parseFloat($("#originalTotal"+shopid).val());
    
    var cost=parseFloat($("#shipment_cost"+shopid).val());
    if($("#shipment"+shopid).find('option:selected').attr('id')==-1)
        total+=cost;
    total=total.toFixed(2);
    var i = total.lastIndexOf('.');
    total=setCharAt(total,i,',');
    
    $("#total"+shopid).html("&euro; "+total);
    
}

function placeOrderCart(shopid)
{
    var data="?shopid="+shopid;
    data+="&shipment="+$("#shipment"+shopid).find('option:selected').attr('id');
    location.href="restricted/order/PlaceOrder"+data;
}