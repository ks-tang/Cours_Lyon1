<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<h1>Utilisateurs</h1>
<ul>
<c:forEach items="${requestScope.list}" var="user">
    <li><a href="${pageContext.request.contextPath}/users/${user}">${user}</a></li>
</c:forEach>
</ul>
</body>
</html>
