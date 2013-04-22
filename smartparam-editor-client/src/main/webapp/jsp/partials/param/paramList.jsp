<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>

<section ng-controller="ParamListController">
    <h1>Registered parameters</h1>

    <table class="table table-hover table-bordered">
        <thead>
            <tr>
                <th>Parameter</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
            <tr ng-repeat="param in params">
                <td>{{param.name}}</td>
                <td>{{param.label}}</td>
            </tr>
        </tbody>
    </table>
</section>