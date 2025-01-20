<%@ page import="java.util.Map" %>
<%@ page import="java.beans.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<jsp:useBean id="user" beanName="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User"></jsp:useBean>
<body>
<header>
    <h1 class="header-titre">Chatons.org</h1>
    <p class="header-user">Bonjour <strong> ${user.name} </strong>,<br> <!--${sessionScope.user.login}-->
        il y a actuellement <%=((Map) (application.getAttribute("users"))).size()%> utilisateur(s) connect&eacute;(s).</p>
</header>

<div class="wrapper">
    <aside class="menu">
        <h2>Menu</h2>
        <div>
            <a href="user.jsp">Mon compte</a>
            <br>
            <a href="deco">D&eacute;connexion</a>
        </div>
    </aside>

    <article class="contenu">
        <h2>Conversation</h2>
        <iframe src="conversation" name="conversation" style="border: none; width: 100%; height: 300px;"></iframe>
        <hr>
        <form method="post" action="conversation" target="conversation">
            <p>
                Message :
                <input type="text" name="text">
                <input type="submit" value="Envoyer">
                <input type="hidden" name="login" value="${sessionScope.user.login}">
            </p>
        </form>
    </article>
</div>

<footer>
    <div>Licence : <a rel="license" href="https://creativecommons.org/licenses/by-nc-sa/3.0/fr/"><img alt="Licence Creative Commons" style="border-width:0; vertical-align:middle;" src="https://i.creativecommons.org/l/by-nc-sa/3.0/fr/88x31.png" /></a></div>
</footer>
</body>
</html>