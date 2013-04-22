<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>

<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>

<html ng-app="smartparam">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="imports.jsp" %>
        <title>SmartParam Editor</title>
    </head>
    <body>
        <div class="container">
            <%@include file="header.jsp" %>
            <%@include file="navigation.jsp" %>

            <div class="row" ng-view>
                
            </div>
        </div>
    </body>
</html>
