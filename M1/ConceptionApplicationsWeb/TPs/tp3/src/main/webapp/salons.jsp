<%@ page import="javax.naming.NameAlreadyBoundException" %>
<%@ page import="javax.naming.NameNotFoundException" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salon" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User"/>
<jsp:useBean id="userDao" scope="application" type="fr.univlyon1.m1if.m1if03.daos.UserDao"/>
<jsp:useBean id="salonDao" scope="application" type="fr.univlyon1.m1if.m1if03.daos.SalonDao"/>
<c:set var="users" value="${userDao.findAll()}"/>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<%  // Traitement des paramètres de la requête
    if(request.getMethod().equals("POST") && request.getParameter("salon") != null) {
        if(request.getParameter("creation") != null) {
            salonDao.add(new Salon(request.getParameter("salon"), user.getLogin()));
            salonDao.findOne(user.getLogin() + "/" + request.getParameter("salon")).addMembre(user);
%>
<p><strong>Le salon ${param.salon} a été créé.</strong></p>
<%
        } else if(request.getParameter("suppression") != null) {
            salonDao.deleteById(user.getLogin() + "/" + request.getParameter("salon"));
%>
<p><strong>Le salon ${param.salon} a été supprimé.</strong></p>
<%
        } else if(request.getParameter("ajoutMembre") != null && request.getParameter("membre") != null){
            try {
                User membre = userDao.findOne(request.getParameter("membre"));
                salonDao.findOne(user.getLogin() + "/" + request.getParameter("salon")).addMembre(membre);
%>
<p><strong>L'utilisateur ${param.membre} a été ajouté au salon ${param.salon}.</strong></p>
<%
            } catch (NameAlreadyBoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } catch (NameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
%>
<h2>Salons</h2>
<h3>Les salons dont vous êtes propriétaire</h3>
<ul>
    <c:forEach items="${salonDao.findAll()}" var="salon">
        <c:if test="${salon.getOwner().equals(user.getLogin())}">
            <li>
                <form method="post">
                    <em><label>Nom : <input type="text" name="salon" value="${salon.getNom()}" readonly></label></em><br>
                    <label>Ajout membre : <input type="text" name="membre"></label> <input type="submit" name="ajoutMembre" value="Ajouter"><br>
                    <input type="submit" name="suppression" value="Supprimer le salon">
                </form>
            </li>
        </c:if>
    </c:forEach>
</ul>

<h3>Créer un nouveau salon</h3>
<form method="post">
    <label>Nom : <input type="text" name="salon"></label>
    <input type="submit" name="creation" value="Créer le salon">
</form>

<h3>Les salons dont vous êtes membre</h3>
<ul>
    <c:forEach items="${salonDao.findAll()}" var="salon">
            <c:if test="${salon.hasMembre(user)}">
                <li>
                    <em>${salon.getNom()} (${salon.getOwner()})</em> :
                    <a href="conversation?owner=${salon.getOwner()}&salon=${salon.getId()}">y aller</a> ou
                    <a href="demandes.jsp?owner=${salon.getOwner()}&salon=${salon.getId()}&user=${user.login}&result=accept&action=quit">quitter ce salon</a>
                </li>
            </c:if>
    </c:forEach>
</ul>

<h3>Les salons dont vous n'êtes pas (encore) membre</h3>
<ul>
    
    <c:forEach items="${salonDao.findAll()}" var="salon">
        <c:if test="${!salon.hasMembre(user)}">
            <li>
                <em>${salon.getNom()} (${salon.getOwner()})</em> :
                <a href="demandes.jsp?owner=${salon.getOwner()}&salon=${salon.getId()}&user=${user.login}&result=accept&action=register">demander à être membre
                    de ce salon</a>
            </li>
        </c:if>
    </c:forEach>
    
</ul>

</body>
</html>
