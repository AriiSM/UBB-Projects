<?php
    session_start();
    require_once('utils.php');
    
    function get_images($id) {
        $conn = get_new_connection();
        $stmt = $conn->prepare('SELECT id, image FROM images WHERE user_id = ?');
        $stmt->bind_param('s', $id);
        $stmt->execute();
        $result = $stmt->get_result()->fetch_all();
        $conn->close();
        return $result;
    }
?>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Main page</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
   <style>
        * {
            font-family: 'Roboto', sans-serif;
            font-size: 20px;
        }
        h1 {
            font-family: 'Pacifico', cursive;
            font-size: 30px;
            color: #1565c0; /* Albastru închis */
        }
        h2 {
            font-family: 'Pacifico', cursive;
            font-size: 25px;
            color: #1976d2; /* Albastru mai deschis */
        }
        .imgContainer {
            display: flex; 
            flex-direction: row; 
            flex-wrap: wrap;
        }
        .imgDiv {
            display: flex; 
            flex-direction: column; 
            align-items: center;
            margin: 10px;
        }
        img {
            width: 150px; 
            height: 150px;
            border-radius: 10px;
        }
        a {
            color: #1976d2; /* Albastru mai deschis */
            text-decoration: none;
            margin-top: 10px;
            display: inline-block;
        }
        a:hover {
            text-decoration: underline;
        }
        fieldset {
            border: 2px solid #1976d2; /* Albastru mai deschis */
            border-radius: 10px;
            padding: 20px;
            background-color: #bbdefb; /* Albastru deschis */
            max-width: 600px;
            margin: 0 auto;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #1976d2; /* Albastru mai deschis */
        }
        input[type="text"],
        input[type="password"],
        input[type="file"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #1976d2; /* Albastru mai deschis */
            border-radius: 5px;
        }
        input[type="submit"] {
            padding: 10px 20px;
            background-color: #1976d2; /* Albastru mai deschis */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #1565c0; /* Albastru închis */
        }
        p {
            color: red;
        }
        hr {
            margin-top: 20px;
            margin-bottom: 20px;
            border: none;
            border-top: 2px solid #1976d2; /* Albastru mai deschis */
        }
    </style>
</head>
<body>
    
</body>
</html>
    <fieldset>
        <legend><strong><h1>My profile</h1></strong></legend>
        <?php
            $id = $_SESSION['id'];

            if (!$id) {
                raise_login_err("You are not logged in!");
            }

            $images = get_images($id);
            echo "<h1>My images</h1>";
            echo "<div class='imgContainer'>";
            foreach ($images as $image)
            {
                echo "<div class='imgDiv'>";
                echo "<img src='".$image[1]."' alt='".$image[0]."' style=''>";
                echo "<a href='delete.php?id=".$image[0]."'>Delete</a>";
                echo "</div>";
            }
            echo "</div>";

            echo "<hr><form action='add.php' method='post' enctype='multipart/form-data'>";
            echo "<label for='file'><h1>Upload an image</h1></label><input type='file' name='file' id='file' accept='image/*'><br><br>";
            echo "<input type='submit' value='Upload'>";
            echo "</form>";

            if (isset($_GET['uploadError'])) {
                echo "<p>".$_GET['uploadError']."</p>";
            }

            echo "<hr><form action='main.php' method='get'>";
            echo "<label for='username'><h1>Search</h1></label><input type='text' name='search' id='username'><br><br>";
            echo "<input type='submit' value='Search'>";
            echo "</form>";
        

            if (isset($_GET['searchError'])) {
                echo "<p>".$_GET['searchError']."</p>";
            }

            if(isset($_GET['search'])) {
                $conn = get_new_connection();
                $username = $_GET['search'];
                $stmt = $conn->prepare('SELECT user_id FROM user WHERE username = ?');
                $stmt->bind_param('s', $username);
                $stmt->execute();
                $result = $stmt->get_result()->fetch_all();
                if (count($result) == 0) {
                    raise_search_err("There was no user found!");
                }

                $id = $result[0][0];
                $stmt = $conn->prepare('SELECT image FROM images WHERE user_id = ?');
                $stmt->bind_param('s', $id);
                $stmt->execute();
                $images = $stmt->get_result()->fetch_all();
                
                echo "<h2>Images of $username</h2>";
                echo "<div class='imgContainer'>";
                foreach ($images as $image) {
                    echo "<div class='imgDiv'>";
                    echo "<img alt='".$image[0]."' src='".$image[0]."'>";
                    echo "</div>";
                }
                echo "</div>";
                $conn->close();
            }

            echo "<hr><a href='logout.php'>Logout</a>";
        ?>
    </fieldset>
</html>
</body>