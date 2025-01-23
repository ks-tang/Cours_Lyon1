<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<header>
    <h1 class="header-titre">Chatons.org</h1>
</header>

<div class="wrapper">
    <aside class="menu">
        <h2>Menu</h2>
        <div>
            <a href="users" target="conversation">Utilisateurs</a>
            <br>
            <a href="salons" target="conversation">Salons</a>
            <br>
            <a href="messages" target="conversation">Messages</a>
        </div>
    </aside>

    <article class="contenu">
        <iframe src="login.html" name="conversation"></iframe>
    </article>
</div>

<footer>
    <div>Licence : <a rel="license" href="https://creativecommons.org/licenses/by-nc-sa/3.0/fr/"><img alt="Licence Creative Commons" style="border-width:0; vertical-align:middle;" src="https://i.creativecommons.org/l/by-nc-sa/3.0/fr/88x31.png" /></a></div>
</footer>
</body>
</html>