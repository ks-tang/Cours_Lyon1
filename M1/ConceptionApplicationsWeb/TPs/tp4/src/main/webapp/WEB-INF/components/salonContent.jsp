<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Salon</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<h1>Salon ${requestScope.dto.name}
    (propriétaire : <a
            href="${pageContext.request.contextPath}/users/${requestScope.dto.owner}">${requestScope.dto.owner}</a>)
</h1>

<p>Il y a actuellement ${requestScope.dto.members.size()} membre(s) dans ce salon.</p>

<h2>Messages</h2>
<table>
    <tr>
        <th>User</th>
        <th>Message</th>
    </tr>
    <c:forEach items="${requestScope.messages}" var="message">
        <tr>
            <td><c:out value="${message.author}"/></td>
            <td><c:out value="${message.text}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
