<%--
    Document   : myself
    Created on : 8-ago-2017, 15.58.18
    Author     : massimo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../common/header.jsp" %>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.4/css/fileinput-rtl.min.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.4/js/fileinput.min.js"></script>
        <title><fmt:message key="user_page"/> - BuyHub</title>
    </head>
    <body >
        <%@include file="../common/navbar.jsp" %>


    <div class="container">
        <div class="row">
            <div class="row">
                <div class="col-md-5">
                    <div class="profile_name">${authenticatedUser.firstName} ${authenticatedUser.lastName}</div>
                    <div class="profile_username">${authenticatedUser.username} </div>

                </div>
                    
                    
            </div>



            <div id="kv-avatar-errors-2" class="center-block" style="width:800px;display:none"></div>
            <form class="form form-vertical" action="avatarUpload" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-sm-4 text-center">
                        <div class="kv-avatar">
                            <div class="file-loading">
                                <input id="avatar-2" name="avatar" type="file" required >
                            </div>
                        </div>
                    </div>
                </div>
            </form>



            <div class="row ">
                <div class="link_box col-md-6 col-centered">
                    <ul class="list-unstyled ">
                        <li> <a href="modifyAccount.jsp" ><fmt:message key="modify_account"/></a> </li>
                        <c:choose>
                            <c:when test="${authenticatedUser.capability eq 2}">
                                <li><a href="myshop.jsp"><fmt:message key="myshop_page"/></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="createNewShop.jsp"><fmt:message key="create_shop"/></a></li>
                            </c:otherwise>
                        </c:choose>
                        <li> <a href="#"><fmt:message key="ask_refund"/></a> </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    
    <script>
var btnCust = '<button type="submit" class="btn btn-secondary" title="Submit" >' +
    '<fmt:message key="confirm"/></i>' +
    '</button>'; 
$("#avatar-2").fileinput({
    overwriteInitial: true,
    maxFileSize: 3000,
    showClose: false,
    showCaption: false,
    showBrowse: false,
    browseOnZoneClick: true,
    removeLabel: '',
    removeIcon: '<i class="glyphicon glyphicon-remove"></i>',
    removeTitle: 'Cancel or reset changes',
    elErrorContainer: '#kv-avatar-errors-2',
    msgErrorClass: 'alert alert-block alert-danger',
    defaultPreviewContent: '<img src="../images/noimage.png" alt="Your Avatar"><h6 class="text-muted">Click to select</h6>',
    layoutTemplates: {main2: '{preview} ' +  btnCust + ' {remove} {browse}'},
    allowedFileExtensions: ["jpg", "png", "gif"]
});
</script>


                
<%@include file="../common/footer.jsp" %>
