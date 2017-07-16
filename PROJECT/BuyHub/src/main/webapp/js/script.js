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
  $('#resizing_select').change(function(){
    $("#width_tmp_option").html($('#resizing_select option:selected').text()); 
    $(this).width($("#width_tmp_select").width());  
  });
});