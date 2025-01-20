<%@ page import="jakarta.servlet.http.HttpServletResponse" %><%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 08/09/2022
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="userDao" type="fr.univlyon1.m1if.m1if03.daos.UserDao" scope="application"/>

<c:if test="${!sessionScope.salon.hasMembre(sessionScope.user)}">
    <% response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas membre de ce salon."); %>
</c:if>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>messages</title>
    <link rel="stylesheet" href="css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<h2>Conversation</h2>

<p>Il y a actuellement ${sessionScope.salon.allMembres.size()} utilisateur(s) connect&eacute;(s).</p>

<table>
    <tr><th>User</th><th>Message</th></tr>
    <c:forEach items="${sessionScope.salon.allMessages}" var="message">
    <tr><td><c:out value="${message.user}"/></td><td><c:out value="${message.text}"/><td></td>
    </c:forEach>
</table>

<hr>
<form method="post" action="conversation">
    <p>
        <label for="text">Message :</label>
        <input type="text" name="text" id="text">
        <input type="submit" value="Envoyer">
        <input type="hidden" name="login" value="${sessionScope.user.login}">
    </p>
</form>

</body>
</html>
