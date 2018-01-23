<%-- 
    Document   : productPhoto
    Created on : 29-dic-2017, 20.40.22
    Author     : Massimo Girondi
--%>

<!DOCTYPE html>
<html>
    <head>
        <%@page import="it.unitn.buyhub.dao.entities.Product"%>
        <%@page import="it.unitn.buyhub.dao.entities.Picture"%>
        <%@page import="java.util.List"%>
        <jsp:useBean id="product" type="Product" scope="request"></jsp:useBean>
        <jsp:useBean id="pictures" type="List<Picture>" scope="request"></jsp:useBean>

        <%@include file="/common/header.jsp" %>
        <title><fmt:message key="product_photo_title"/> - BuyHub</title>
    </head>
    <body>
        <%@include file="/common/navbar.jsp" %>


        <div class="row">
            <div class="col-md-8">

                <h1><fmt:message key="product_photo_title"/></h1>
                <br/>
                <h3><fmt:message key="product_photo_productName"/> :  ${product.name} (ID: ${product.id})</h3>
            </div>
        </div> 

        <div class="row">
            <c:forEach items="${pictures}" var="picture">
                <div class="col-md-4 productPhotoCol">
                    <div class="productPhoto_box">
                        <img class="media-object img-rounded img-responsive productPhoto_img" src="../${picture.path}" alt="product image" />
                    </div>
               <!-- <a href="removeProductPhoto?id=${picture.id}" class="productPhoto_btn btn btn-danger avatar_button" ><fmt:message key="remove_product_picture"/>"</a>-->
                </div>
            </c:forEach>
        </div>

        <div class="col-md-4 col-centered">

            <div class="row">
                <h3><fmt:message key="product_photo_add"/></h3>
            </div>
            <div class="row avatar_box">
            </div>

            <div class="row avatar_box">
                <img class="img-rounded img-responsive" id="picture" src="../images/noimage.png" alt="your image" />
            </div>
            <form method="POST" enctype="multipart/form-data" action="productPictureUpload">

                <div class="row">
                    <input type="file" id="picture_field" name="picture" style="display: none;" />
                    <input type="button" class="btn btn-primary avatar_button" value="<fmt:message key="select_product_picture"/>" onclick="document.getElementById('picture_field').click();" />
                </div>
                <div class="row">
                    <input type="submit" id="submitBTN" disabled="disabled" class="btn btn-success avatar_button" value="<fmt:message key="save_picture"/>"/>  
                </div>
                <input type="hidden" name="product" value="${product.id}"/>
            </form>
        </div>



        <script>
            function readURL(input) {

                if (input.files && input.files[0]) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        $('#picture').attr('src', e.target.result);
                    }

                    reader.readAsDataURL(input.files[0]);
                }
            }

            $("#picture_field").change(function () {
                readURL(this);
                $("#submitBTN").attr('disabled', false);
            });
        </script>


        <%@include file="../common/footer.jsp" %>
