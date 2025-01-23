<?php session_start(); ?>
<html>
<head>
    <title>Hello app</title>
</head>
<body>
<h1>
<?php
if(isset($_POST["login"])) {
    $_SESSION["login"] = $_POST["login"];
    header('Location: /hello.php');
} else {
    header('Location: /login.html');
}
?>
</h1>
</body>
</html>