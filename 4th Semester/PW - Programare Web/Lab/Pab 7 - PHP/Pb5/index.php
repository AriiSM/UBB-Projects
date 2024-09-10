<?php
  session_start();
  if (isset($_SESSION['id'])) {
    header("Location: main.php");
    die();
  }
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login Page</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
  <style>
    * {
      font-size: 20px;
      font-family: 'Pacifico', cursive;
    }
    body {
      background-color: #fce4ec;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    fieldset {
      border: 2px solid #d81b60;
      border-radius: 10px;
      padding: 20px;
      background-color: #f8bbd0;
    }
    legend {
      font-weight: bold;
      color: #880e4f;
    }
    label {
      margin-bottom: 5px;
      display: block;
    }
    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 8px;
      margin-bottom: 10px;
      border: 1px solid #d81b60;
      border-radius: 5px;
    }
    input[type="submit"] {
      padding: 10px 20px;
      background-color: #d81b60;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #880e4f;
    }
    p {
      color: red;
    }
  </style>
</head>
<body>
  <fieldset>
    <legend><strong>Login</strong></legend>
    <form action="login.php" method="post">          
      <label for="username">Username:</label>
      <input type="text" name="username" id="username"><br>
      <label for="password">Password:</label>      
      <input type="password" name="password" id="password"><br>
      <?php
        if (isset($_GET["loginError"])) {
          echo "<p>".$_GET["loginError"]."</p>";
        }
      ?>
      <input type="submit" value="Login">
    </form>
  </fieldset>
</body>
</html>