<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Mon compte</h2>
<form method="post" action="chat" target="_top">
    <p>
        <label>Login : <input type="text" name="login" value="${sessionScope.user.login}" readonly disabled></label><br>
        <label>Prénom : <input type="text" name="name" value="${sessionScope.user.name}"></label><br>
        <input type="hidden" name="action" value="update" />
        <input type="submit" value="Modifier">
    </p>
</form>
</body>
</html>
