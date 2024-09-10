<?php
    require_once('utils.php');
    function get_comentarii() {
        $conn = get_new_connection();
        $stmt = $conn->prepare('SELECT autor, text FROM comentarii WHERE accepted = 1 ORDER BY id DESC');
        $stmt->execute();
        return $stmt->get_result()->fetch_all();
    }
?>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iubire&fericire</title>
    <style>
            body {
        font-family: 'Roboto', sans-serif;
        background-color: #4169e1; /* RoyalBlue */
        color: #fff; /* White */
        margin: 0;
        padding: 0;
    }

    .container {
        max-width: 800px;
        margin: 20px auto;
        padding: 20px;
        background-color: #fff; /* White */
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    /* Articole */
    .article {
        padding: 20px;
        border-bottom: 1px solid #87ceeb; /* SkyBlue */
    }

    .article h1 {
        font-size: 24px;
        color: #fff; /* White */
        margin-bottom: 10px;
    }

    .article h3 {
        font-size: 18px;
        color: #fff; /* White */
        margin-bottom: 5px;
    }

    .article p {
        font-size: 16px;
        color: #f0f8ff; /* AliceBlue */
        margin-bottom: 20px;
    }

    /* Comentarii */
    .comment {
        padding: 10px;
        background-color: #87ceeb; /* SkyBlue */
        border-radius: 5px;
        margin-bottom: 10px;
    }

    .comment h3 {
        font-size: 16px;
        color: #fff; /* White */
        margin-bottom: 5px;
    }

    .comment p {
        font-size: 14px;
        color: #f0f8ff; /* AliceBlue */
        margin-bottom: 0;
    }

    /* Formular comentarii */
    .comment-form {
        padding: 20px;
        border-top: 1px solid #87ceeb; /* SkyBlue */
    }

    .comment-form label {
        display: block;
        font-size: 16px;
        color: #fff; /* White */
        margin-bottom: 5px;
    }

    .comment-form input[type="text"],
    .comment-form textarea {
        width: 100%;
        padding: 10px;
        margin-bottom: 10px;
        border: 1px solid #fff; /* White */
        border-radius: 5px;
        box-sizing: border-box;
        background-color: #4169e1; /* RoyalBlue */
        color: #fff; /* White */
    }

    .comment-form input[type="submit"] {
        padding: 10px 20px;
        background-color: #fff; /* White */
        color: #4169e1; /* RoyalBlue */
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
    }

    .comment-form input[type="submit"]:hover {
        background-color: #87ceeb; /* SkyBlue */
    }

    /* Mesaje de eroare */
    .error {
        color: red;
        font-size: 14px;
        margin-top: 10px;
    }
        </style>
</head>
<body>
    <fieldset>
        <legend><h1>Iubire&psihologie</h1></legend>
        <?php
            include 'articol.php';
            echo '<hr><h3>Comentarii</h3>';
            $comentarii = get_comentarii();
            if(!$comentarii) {
                echo '<p>Nu exista comentarii</p>';
            }
            else {
                foreach ($comentarii as $comentariu) {
                    echo '<fieldset><legend><h3>'.$comentariu[0].'</h3></legend><p>'.$comentariu[1].'</p></fieldset>';
                }
            }
            echo '<hr><h3>Adauga comentariu</h3>';
            echo '<form action="add.php" method="post">';
            echo '<label for="autor">Autor:</label>';
            echo '<input type="text" name="autor" id="autor"><br>';
            echo '<label for="text">Text: </label>';
            echo '<textarea name="text" id="text" cols="30" rows="5"></textarea><br>';
            echo '<input type="submit" value="Trimite">';
            echo '</form>';
            if (isset($_GET["error"])) {
                echo "<p>".$_GET["error"]."</p>";
            }
            echo '<hr><a href="login.php">Admin</a><br>';
        ?>
    </fieldset>
</body>
</html>
<?php    
    
?>