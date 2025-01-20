<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Salons</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<h1>Salons</h1>
<h2>Liste de salons</h2>
<ul>
    <c:forEach items="${requestScope.list}" var="salon">
        <li><a href="${pageContext.request.contextPath}/salons/${salon}">${salon}</a></li>
    </c:forEach>
</ul>

</body>
</html>
