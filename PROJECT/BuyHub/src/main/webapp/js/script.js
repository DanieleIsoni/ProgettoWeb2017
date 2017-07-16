/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
    
    $("time.timeago").timeago();
});


$(document).ready(function() {
  $('#resizing-select').change(function(){
    $("#width-tmp-option").html($('#resizing-select option:selected').text()); 
    $(this).width($("#width-tmp-select").width());  
  });
});