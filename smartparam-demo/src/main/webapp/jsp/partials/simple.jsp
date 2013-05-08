<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>

<h1>Simple mapping</h1>
<p>
    Simple mapping parameter consists of single input value that produces single
    output value.
</p>
<p>${output}</p>
<table class="table table-hover table-bordered">
    <thead>
        <tr>
            <th>Input value (hero)</th>
            <th>Output value (identity)</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Superman</td>
            <td>Clark Kent</td>
        </tr>
        <tr>
            <td>Batman</td>
            <td>Bruce Wayne</td>
        </tr>
    </tbody>
</table>
