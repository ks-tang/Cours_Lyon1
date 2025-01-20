<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Chatons !</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h1>Mon compte</h1><br>
<form method="post" action="chat">
    <p>
        Modifier votre compte :<br>
            <label>Login : <input type="text" value=" ${sessionScope.user.login}" readonly="readonly" ></label><br>
            <label>Prénom : <input type="text" name="userID" value="${sessionScope.user.name}" ></label><br>
            <input type="submit" value="Modifier">
    </p>
</form>
</body>
</html>