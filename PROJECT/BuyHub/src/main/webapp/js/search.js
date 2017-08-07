$(function() {
    
    //se arrivo cercando dalla barra di ricerca carico i dati dalla url nel form
    $("#search_text").val(getUrlParameter("q").replace(/\+/g," "));
    $("#search_category").val(getUrlParameter("c")?getUrlParameter("c"):-1);
   
    $(".range").ionRangeSlider({
        hide_min_max: true,
        keyboard: true,
        type: 'double',
        step: 1,
        grid: true,
        onFinish: slide_change
    });


    $(".single-range").ionRangeSlider({
        hide_min_max: true,
        step: 1,
        onFinish: slide_change
    });

    //invoco la prima ricerca
    cerca();
    
});

function cerca()
{
    var q=$("#search_text").val();
    var c=$("#search_category").val();
    var minRev=$("#minRev").val();
    var price=$("#price").val().split(";");
    var min=price[0];
    var max=price[1];
    
    $.ajax({
        method: "POST",
        url: "search",
        data: { "q": q,
                "c": c,
                "minRev": minRev,
                "min": min,
                "max": max
            }
      })
        .done(function( msg ) {
            var products=$("#products");
            products.empty();
            for (var i in msg.products) {
            //    for(var i=0;i<10;i++){
            var p=msg.products[i];
                
                var s="<div class='row search_item'>\n";
                s+="<div class='media'>";
                s+="<div class='media-left'>";
                s+="<a href='#'>";
                s+="<div class='search_img_box'><img class='media-object img-rounded img-responsive' src='"+p.mainPicture.path+"' alt='"+p.name+"'></div>";
                s+="</a>";
                s+="</div>";
                s+="<div class='media-body'>";
                s+="<h4 class='media-heading'><a href='product?id="+p.id+"'>"+p.name+"</a></h4>";
                s+="<div class='item_price'>"+p.price+"€</div>";
                s+="<div class='shop_name'><a href='shop?id="+p.shop.id+"'>"+p.shop.name+"</a></div>";
                
                
                s+="</div></div></div><hr/>\n";
                
                //products.append(p.name);
                products.append(s);
                
                
                //aggiungere prodotti alla lista
            }
            
            //stampare barra con i numeri di pagina
            
            products.append("\n<br/>\nPagina"+msg.p+" di "+msg.pages);
        });



    
}

function slide_change(data)
{
    cerca();
}

 