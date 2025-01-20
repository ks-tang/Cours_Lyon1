<%@ page import="fr.univlyon1.m1if.m1if03.classes.Demande" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salon" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page import="javax.naming.NameAlreadyBoundException" %>
<%@ page import="javax.naming.NameNotFoundException" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User"/>
<jsp:useBean id="userDao" scope="application" type="fr.univlyon1.m1if.m1if03.daos.UserDao"/>
<jsp:useBean id="salonDao" scope="application" type="fr.univlyon1.m1if.m1if03.daos.SalonDao"/>
<jsp:useBean id="demandeDao" scope="application" type="fr.univlyon1.m1if.m1if03.daos.DemandeDao"/>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Mes demandes</h2>

<p>Cette page contient uniquement les demandes qui vous ont été adressées. Celle contenant les demandes que vous avez envoyées n'a pas été implémentée...</p>

<% // Traitement des paramètres de la requête
    demandeDao.createDemande(request.getParameter("user"), request.getParameter("owner"), request.getParameter("salon"), request.getParameter("action"));
    List<Demande> userDemandes = demandeDao.getDemandesByUser(user.getLogin());

    if (request.getParameter("owner") != null && request.getParameter("salon") != null && request.getParameter("user") != null && request.getParameter("action") != null) {
        /*try {
            String ownerLogin = userDao.findOne(request.getParameter("owner")).getLogin();
            List<Demande> ownerDemandes = demandeDao.getDemandesByUser(ownerLogin);
            demandeDao.createDemande(request.getParameter("user"), ownerLogin, request.getParameter("salon"), request.getParameter("action"));
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }*/
%>
<p><strong>Votre demande a été transmise.</strong></p>
<%
    }
    if (request.getParameter("idDemande") != null && request.getParameter("result") != null) {
        try {
            Demande demande = demandeDao.findOne(request.getParameter("idDemande"));
            switch (request.getParameter("result")) {
                case "accept":
                    switch (demande.getAction()) {
                        case "register":
                            salonDao.findOne(demande.getSalon()).addMembre(userDao.findOne(demande.getUser()));
                            break;
                        case "quit":
                            salonDao.findOne(demande.getSalon()).removeMembre(userDao.findOne(demande.getUser()));
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    demande.accept();
                    demandeDao.deleteById(request.getParameter("idDemande"));
                    break;
                case "refuse":
                    demande.refuse();
                    demandeDao.deleteById(request.getParameter("idDemande"));
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
%>
<p><strong>Votre réponse a été enregistrée.</strong></p>
<%
        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NameNotFoundException e) {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }
%>
<table>
    <thead>
    <tr>
        <td>Auteur</td>
        <td>Salon</td>
        <td>Action demandée</td>
        <td>(&Eacute;tat)</td>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="<%=userDemandes%>" var="demande">
        <c:set var="i" value="${demande.getId()}" />
        <tr>
            <td>${demande.user}</td>
            <td>${demande.salon}</td>
            <td>${demande.action}</td>
            <td>(${demande.state})</td>
            <c:if test="${demande.state.equals('En cours')}">
                <td><a href="demandes.jsp?idDemande=${demande.getId()}&result=accept">accepter</a></td>
                <td><a href="demandes.jsp?idDemande=${demande.getId()}&result=refuse">refuser</a></td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
