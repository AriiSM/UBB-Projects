<?php
    session_start();
    require_once('utils.php');

    $id = $_SESSION['id'];
    if (!$id) {
        raise_login_err("You need to login first!");
    }

    function get_comentarii() {
        $conn = get_new_connection();
        $stmt = $conn->prepare('SELECT id, autor, text, accepted FROM comentarii ORDER BY id DESC');
        $stmt->execute();
        return $stmt->get_result()->fetch_all();
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Page</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&family=Pacifico&display=swap" rel="stylesheet">
    <style>
   body {
    font-family: 'Roboto', sans-serif;
    background-color: #4169e1; /* RoyalBlue */
    color: #fff; /* Alb */
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

/* Articole */
.article {
    padding: 20px;
    border-bottom: 1px solid #87ceeb; /* SkyBlue */
}

.article h1 {
    font-size: 24px;
    color: #4169e1; /* RoyalBlue */
    margin-bottom: 10px;
}

.article h3 {
    font-size: 18px;
    color: #4169e1; /* RoyalBlue */
    margin-bottom: 5px;
}

.article p {
    font-size: 16px;
    color: #fff; /* Alb */
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
    color: #4169e1; /* RoyalBlue */
    margin-bottom: 5px;
}

.comment p {
    font-size: 14px;
    color: #fff; /* Alb */
    margin-bottom: 0;
}

/* Formular de comentarii */
.comment-form {
    padding: 20px;
    border-top: 1px solid #87ceeb; /* SkyBlue */
}

.comment-form label {
    display: block;
    font-size: 16px;
    color: #4169e1; /* RoyalBlue */
    margin-bottom: 5px;
}

.comment-form input[type="text"],
.comment-form textarea {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #4169e1; /* RoyalBlue */
    border-radius: 5px;
    box-sizing: border-box;
    background-color: #fff; /* Alb */
    color: #4169e1; /* RoyalBlue */
}

.comment-form input[type="submit"] {
    padding: 10px 20px;
    background-color: #4169e1; /* RoyalBlue */
    color: #fff; /* Alb */
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
        <legend><h2>Comentarii</h2></legend>
        <?php
            $comentarii = get_comentarii();
            foreach ($comentarii as $comentariu) {
                $class = '';
                if($comentariu[3] == 1) {
                    $class = 'accepted';
                }
                else if($comentariu[3] == 2) {
                    $class = 'rejected';
                }    
                
                echo '<fieldset class="'.$class.'"><legend><h3>'.$comentariu[1].'</h3></legend><p>'.$comentariu[2].'</p>';
                echo '<form action="approve.php" method="post">';
                echo '<input type="hidden" name="id" value="'.$comentariu[0].'">';
                echo '<input type="hidden" name="accepted" value="1">';
                echo '<input type="submit" value="Accept">';
                echo '</form>';
                echo '<form action="approve.php" method="post">';
                echo '<input type="hidden" name="id" value="'.$comentariu[0].'">';
                echo '<input type="hidden" name="accepted" value="2">';
                echo '<input type="submit" value="Reject">';
                echo '</form>';
                echo '</fieldset>';
            }
        ?>
        <br><hr>
        <a href="index.php">Back</a><br>
        <a href="logout.php">Logout</a>
    </fieldset>
</body>
</html>