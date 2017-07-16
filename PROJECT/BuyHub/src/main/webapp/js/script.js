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
    
    $(".range").ionRangeSlider({
        hide_min_max: true,
        keyboard: true,
        type: 'double',
        step: 1,
        grid: true
    });


    $(".single-range").ionRangeSlider({
        hide_min_max: true,
        step: 1
    });

    $("#search_text").typeahead({
	    source:  function (query, process) {
                return $.getJSON('autocomplete', { term: query , cat: $("#search_category").val()}, function (data) {
                                console.log(data.res);
                                
                                data = data.res;

                            return process(data);
                        });
                    },
            minLength: 3,
            items:8,
            matcher: function (item) {
                // to display not matching items
                    return true;
                }
                });

    
    
    
    
});
