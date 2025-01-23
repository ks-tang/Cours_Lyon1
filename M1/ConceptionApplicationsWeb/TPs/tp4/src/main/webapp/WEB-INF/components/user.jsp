<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<h1>Utilisateur ${requestScope.dto.login}</h1>
<ul>
    <li>Login : ${requestScope.dto.login}</li>
    <li>Nom : ${requestScope.dto.name}</li>
    <li>Propriétaire de :
        <c:forEach items="${requestScope.dto.ownedSalons}" var="salon"><a href="${pageContext.request.contextPath}/salons/${salon}">${salon}</a>&nbsp;</c:forEach>
    </li>
    <li>Membre de :
        <c:forEach items="${requestScope.dto.memberOfSalons}" var="salon"><a href="${pageContext.request.contextPath}/salons/${salon}">${salon}</a>&nbsp;</c:forEach>
    </li>
    <li>Auteur de :
        <c:forEach items="${requestScope.dto.createdMessages}" var="message"><a href="${pageContext.request.contextPath}/messages/${message}">${message}</a>&nbsp;</c:forEach>
    </li>
</ul>
</body>
</html>
