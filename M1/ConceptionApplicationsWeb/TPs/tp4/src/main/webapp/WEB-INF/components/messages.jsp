<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>messages</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<h1>Messages</h1>
<h2>Liste de messages</h2>
<ul>
    <c:forEach items="${requestScope.list}" var="message">
        <li><a href="${pageContext.request.contextPath}/messages/${message}">${message}</a></li>
    </c:forEach>
</ul>

</body>
</html>
