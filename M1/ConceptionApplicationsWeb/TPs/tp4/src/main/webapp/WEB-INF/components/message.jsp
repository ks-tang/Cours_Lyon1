<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Message</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<h1>Message</h1>

<c:if test="${requestScope.dto.salon != null}">
    <h2>Salon</h2>
    <p><a href="${pageContext.request.contextPath}/salons/${requestScope.dto.salon}">${requestScope.dto.salon}</a></p>
</c:if>

<c:if test="${requestScope.dto.author != null}">
    <h2>Auteur</h2>
    <p><a href="${pageContext.request.contextPath}/users/${requestScope.dto.author}">${requestScope.dto.author}</a></p>
</c:if>

<c:if test="${requestScope.dto.text != null}">
    <h2>Texte</h2>
    <p><c:out value="${requestScope.dto.text}"/></p>
</c:if>

</body>
</html>
