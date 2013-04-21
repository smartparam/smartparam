<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>

<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>

<html ng-app="smartparam.demo">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="imports.jsp" %>
        <title>SmartParam Demo</title>
    </head>
    <body>
        <div class="container">
            <%@include file="header.jsp" %>

            <div class="row">
                <div class="span3">
                    <%@include file="menu.jsp" %>
                </div>
                <div class="span9">
                    <%@include file="sections.jsp" %>
                </div>
            </div>
        </div>
    </body>
</html>
