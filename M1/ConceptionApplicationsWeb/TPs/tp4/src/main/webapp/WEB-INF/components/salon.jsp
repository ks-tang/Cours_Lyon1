<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Salon</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<h1>Salon
    <c:if test="${requestScope.dto.name != null}">
        ${requestScope.dto.name}
    </c:if>
</h1>

<c:if test="${requestScope.dto.owner != null}">
    <h2>Propriétaire</h2>
    <p>
        <a href="${pageContext.request.contextPath}/users/${requestScope.dto.owner}">${requestScope.dto.owner}</a>
    </p>
</c:if>

<c:if test="${requestScope.dto.members != null}">
    <h2>Membres</h2>
    <ul>
        <c:forEach items="${requestScope.dto.members}" var="member">
            <li><a href="${pageContext.request.contextPath}/users/${member}">${member}</a></li>
        </c:forEach>
    </ul>
</c:if>

<c:if test="${requestScope.dto.messages != null}">
    <h2>Messages</h2>
    <ul>
        <c:forEach items="${requestScope.dto.messages}" var="messageId">
            <li><a href="${pageContext.request.contextPath}/messages/${messageId}">${messageId}</a></li>
        </c:forEach>
    </ul>
</c:if>

<p><a href="${pageContext.request.requestURI}/content">Contenu</a></p>

</body>
</html>
