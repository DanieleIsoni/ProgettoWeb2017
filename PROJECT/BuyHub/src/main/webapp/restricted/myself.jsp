<%--
    Document   : myself
    Created on : 8-ago-2017, 15.58.18
    Author     : Massimo Girondi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="err" uri="/WEB-INF/tld/errors.tld" %>
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
                <div class="col-md-5">
                    <div class="profile_name">${authenticatedUser.firstName} ${authenticatedUser.lastName}</div>
                    <div class="profile_username">${authenticatedUser.username} </div>

                </div>


            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="row avatar_box">
                        <img class="img-rounded img-responsive" id="avatar" src="../${authenticatedUser.avatar}" alt="your image" />
                    </div>
                    <div class="row">
                        <form method="POST" enctype="multipart/form-data" action="avatarUpload">

                            <!--<input type="file" name="avatar" id="avatar_field"/>-->
                            <!--Use a javascript code to avoid show file name-->
                            <input type="file" id="avatar_field" name="avatar" style="display: none;" />
                            <input type="button" class="btn btn-primary avatar_button" value="<fmt:message key="select_file"/>" onclick="document.getElementById('avatar_field').click();" />
                    </div>
                    <div class="row">
                        <input type="submit" class="btn btn-success avatar_button" value="<fmt:message key="change_avatar"/>"/>  
                    </div>
                    <div class="row">
                        <input type="submit" class="btn btn-danger avatar_button" name="remove" value="<fmt:message key="remove_avatar"/>">

                        </form>
                    </div>
                </div>


                <div class="col-md-6">

                    <div class="row ">
                        <div class="link_box col-md-6 col-centered">
                            <ul class="list-unstyled ">
                                <li> <a href="modifyAccount.jsp" ><fmt:message key="modify_account"/></a> </li>
                                    <c:choose>
                                        <c:when test="${authenticatedUser.capability eq 2}">
                                        <li><a href="myshop.jsp"><fmt:message key="myshop_page"/></a></li>
                                        </c:when>
                                        <c:when test="${authenticatedUser.capability < 2}">
                                        <li><a href="createNewShop.jsp"><fmt:message key="create_shop"/></a></li>
                                        </c:when>
                                    </c:choose>
                                <li> <a href="myorders"><fmt:message key="myorders_title"/></a> </li>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
            <div class="row">
                <err:ErrorMessage page="myself"/>
            </div>
        </div>
    </div>

    <script>
        function readURL(input) {

            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    $('#avatar').attr('src', e.target.result);
                }

                reader.readAsDataURL(input.files[0]);
            }
        }

        $("#avatar_field").change(function () {
            readURL(this);
        });
    </script>

    <%@include file="../common/footer.jsp" %>
