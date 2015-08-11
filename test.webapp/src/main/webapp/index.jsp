<html>
<body>
<h2>Hello World!</h2>
<%
Integer count = (Integer)session.getAttribute("count");
if (count == null) {
    session.setAttribute("count",1);
}
else {
    session.setAttribute("count", count+1);
}
%>
<%=request.getServerPort()%>
<table>
    <tr>
        <th>ID</th>
        <th>count</th>
    </tr>
    <tr>
        <td> <%=session.getId()%></td>
        <td> <%=session.getAttribute("count")%></td>
    </tr>
</table>
</body>
</html>
