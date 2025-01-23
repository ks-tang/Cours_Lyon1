<?php session_start(); ?>
<html>
<head>
    <title>Hello app</title>
</head>
<body>
<h1>
<?php
if(isset($_SESSION["login"])) {
    echo ($_SESSION["login"] . " vous dit : ");
}

if(isset($_POST["name"])) {
    if(isset($_POST["lang"]) && $_POST["lang"] === "fr"){
        echo ("Bonjour " . htmlspecialchars($_POST["name"]) . " !");
        } elseif(isset($_POST["lang"]) && $_POST["lang"] === "en") {
            echo ("Hi " . htmlspecialchars($_POST["name"]) . "!");
        } else {
            echo ("Buongiorno " . htmlspecialchars($_POST["name"]) . " !");
        }
    } else {
        echo ("Hello");
}
?>
</h1>
<p><a href="/hello.html">Retour au formulaire</a></p>
</body>
</html>
