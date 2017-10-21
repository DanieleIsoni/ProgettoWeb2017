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
        <link href="../css/bootstrap-imageupload.min.css"/>
        
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

                    <img id="avatar" src="../${authenticatedUser.avatar}" alt="your image" />
                    <form method="POST" enctype="multipart/form-data" action="avatarUpload">
                        
                        <input type="file" name="avatar" id="avatar_field"/>
                        
                        <input type="submit"/>  
                        
                        <input type="submit" name="remove" value="remove">
                        
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
                        function readURL(input) {

                        if (input.files && input.files[0]) {
                          var reader = new FileReader();

                          reader.onload = function(e) {
                            $('#avatar').attr('src', e.target.result);
                          }

                          reader.readAsDataURL(input.files[0]);
                        }
                      }

                      $("#avatar_field").change(function() {
                        readURL(this);
                      });
                        </script>
                
<%@include file="../common/footer.jsp" %>
