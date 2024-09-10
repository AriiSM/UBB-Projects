<?php
  session_start();
  if (isset($_SESSION["id"])) {
    header("Location: admin.php");
    die();
  }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
   <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f0f0f0; /* Gri deschis */
            color: #333; /* Negru */
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff; /* Alb */
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .article {
            padding: 20px;
            border-bottom: 1px solid #ddd; /* Gri deschis */
        }
        .article h1 {
            font-size: 24px;
            color: #333; /* Negru */
            margin-bottom: 10px;
        }
        .article h3 {
            font-size: 18px;
            color: #333; /* Negru */
            margin-bottom: 5px;
        }
        .article p {
            font-size: 16px;
            color: #666; /* Gri */
            margin-bottom: 20px;
        }
        .comment {
            padding: 10px;
            background-color: #f9f9f9; /* Gri deschis */
            border-radius: 5px;
            margin-bottom: 10px;
        }
        .comment h3 {
            font-size: 16px;
            color: #333; /* Negru */
            margin-bottom: 5px;
        }
        .comment p {
            font-size: 14px;
            color: #666; /* Gri */
            margin-bottom: 0;
        }
        .comment-form {
            padding: 20px;
            border-top: 1px solid #ddd; /* Gri deschis */
        }
        .comment-form label {
            display: block;
            font-size: 16px;
            color: #333; /* Negru */
            margin-bottom: 5px;
        }
        .comment-form input[type="text"],
        .comment-form textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd; /* Gri deschis */
            border-radius: 5px;
            box-sizing: border-box;
        }
        .comment-form input[type="submit"] {
            padding: 10px 20px;
            background-color: #333; /* Negru */
            color: #fff; /* Alb */
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .comment-form input[type="submit"]:hover {
            background-color: #666; /* Gri */
        }
        .error {
            color: red;
            font-size: 14px;
            margin-top: 10px;
        }
    </style>
</head>
<body>
  <fieldset>
    <legend><h2>Login</h2></legend>
    <form action="login.php" method="post">
      <label for="username">Username:</label>
      <input type="text" name="username" id="username"><br>
      <label for="password">Password:</label>
      <input type="password" name="password" id="password"><br>
      <input type="submit" value="Login">
    </form>
    <?php
      if (isset($_GET["loginError"])) {
        echo "<p>".$_GET["loginError"]."</p>";
      }
    ?>
    <a href="index.php">Go back to the article!</a>
  </fieldset>
</body>
</html>