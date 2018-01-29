<%--
    Document   : modifyAccount
    Created on : Sep 23, 2017, 8:28:27 AM
    Author     : Daniele Isoni
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>

        <%@include file="../common/header.jsp" %>
        <title><fmt:message key="mod_account_title"/> - BuyHub</title>
    </head>
    <body >
         <%@include file="../common/navbar.jsp" %>
           
        <div class="text-center login">
            <img src="../images/icon.png" alt="BuyHub logo" height="42" width="42">
            <h3><fmt:message key="mod_account_desc"/></h3>
            <br>
            <div class="panel panel-default panel-footer">
                <err:ErrorMessage page="modifyAccount"/>
                <form method="POST" id="modAccount-form" action="ModifyAccount">
                    <div class="form-group">
                        <label for="first_name"><fmt:message key="first_name"/>:</label>
                        <input type="text" name="first_name" value=${authenticatedUser.firstName} class="form-control" id="first_name" disabled>
                    </div>
                    <div class="form-group">
                        <label for="last_name"><fmt:message key="last_name"/>:</label>
                        <input type="text" name="last_name" value=${authenticatedUser.lastName} class="form-control" id="last_name" disabled>
                    </div>
                    <div class="form-group">
                        <label for="email"><fmt:message key="email_address"/>:</label>
                        <input type="email" name="email" value=${authenticatedUser.email} class="form-control" id="email" disabled>
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="old_password"/>:</label>
                        <input type="password" name="old_password" class="form-control" id="old_password">
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="new_password"/>:</label>
                        <input type="password" name="new_password" class="form-control" id="new_password">
                    </div>
                    <div class="form-group">
                        <label><fmt:message key="repeat_new_password"/>:</label>
                        <input type="password" name="new_password2" class="form-control" id="new_password2">
                    </div>

                    <button type="submit" class="btn btn-success"><fmt:message key="save_changes"/></button>
                </form>
            </div>
        </div>
        <%@include file="../common/footer.jsp" %>
