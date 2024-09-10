<?php
    session_start();
    $_SESSION["teacherid"] = null;
?>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>School</title>
   <style>
    * {
        font-size: 20px;
        font-family: 'Roboto', sans-serif;
        color: black; 
    }

    body {
        background-color: #2196F3; /* Fundal albastru */
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        margin: 0; /* Eliminăm marginile pentru a umple complet ecranul */
    }

    fieldset {
        border: 2px solid #64B5F6; /* Bordură albastru deschis */
        border-radius: 10px;
        padding: 20px;
        background-color: #BBDEFB; /* Fundal albastru deschis */
        margin-bottom: 20px;
    }

    legend {
        font-family: 'Pacifico', cursive;
        color: #fff; /* Text alb */
    }

    input[type="text"],
    input[type="password"] {
        width: 200px;
        padding: 5px;
        margin-bottom: 10px;
        border-radius: 10px;
        background-color: #1976D2; /* Fundal albastru închis */
        color: black;
        border: none;
    }

    input[type="text"]::placeholder,
    input[type="password"]::placeholder {
        color: #fff; /* Text alb pentru placeholder */
    }

    input[type="submit"] {
        width: 200px;
        padding: 10px 20px;
        background-color: #1976D2; /* Fundal albastru închis */
        color: black;
        border: none;
        cursor: pointer;
        border-radius: 10px;
    }

    input[type="submit"]:hover {
        background-color: #0D47A1; /* Fundal albastru mai închis la hover */
    }
</style>
</head>
<body>
    <fieldset>
        <legend><strong>Search student grades</strong></legend>
        <form action="students.php" method="get">
            <label for="firstName">First name: </label>
            <input name="firstName" id="firstName" /><br/>
            <label for="lastName">Last name: </label>
            <input name="lastName" id="lastName" /><br/>
            <input type="submit" value="Search" />
        </form>
        <?php
            if (isset($_GET['studentError'])) {
                echo "<p>Invalid student data!</p>";
            }
        ?>
    </fieldset>
    <fieldset>
        <legend><strong>Login as teacher</strong></legend>
        <form action="teachers.php" method="post">
            <label for="username">Username: </label>
            <input name="username" id="username" /><br/>
            <label for="password">Password: </label>
            <input name="password" id="password" type="password"/><br/>
            <input type="submit" value="Login" />
        </form>
        <?php
            if (isset($_GET['loginError'])) {
                echo '<p>'.$_GET['loginError'].'</p>';
            }
        ?>
    </fieldset>
</body>
</html>