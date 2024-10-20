<?php
    session_start();
    require_once('utils.php');
?>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher</title>
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
            color: black;
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
    <?php get_content(); ?>
</body>
</html>

<?php
    function get_teacher_by_username_and_password($username, $password) {
        $conn = get_new_connection();      
        if ($username == "" || $password == "") {
            raise_login_err("Username and password could not be empty!");
        }
        $stmt = $conn->prepare("SELECT teacher_id FROM teachers WHERE username = ? AND password = ?");
        $stmt->bind_param('ss', $username, $password);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($result->num_rows == 0) {
            raise_login_err("The username or the password is invalid!");
        }
        $row = $result->fetch_assoc();
        $teacherid = $row['teacher_id'];
        $conn->close();
        return $teacherid;
    }

    function get_teacher_by_id($teacherid) {
        $conn = get_new_connection();
        if (!is_numeric($teacherid)) {
            
        }
        $stmt = $conn->prepare("SELECT username FROM teachers WHERE teacher_id = ?");
        $stmt->bind_param('i', $teacherid);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($result->num_rows == 0) {
            raise_login_err("The session is invalid!");
        }
        $row = $result->fetch_assoc();
        $username = $row['username'];
        $conn->close();
        return $username;
    }

    function get_subjects($teacherid) {
        $conn = get_new_connection();
        $stmt = $conn->prepare("SELECT ts.subject_id AS subject_id, s.name AS name 
                                FROM teachers_subjects ts 
                                INNER JOIN subjects s ON s.subject_id = ts.subject_id
                                WHERE teacher_id = ?");
        $stmt->bind_param('i', $teacherid);
        $stmt->execute();
        $result = $stmt->get_result();
        echo "<label for='subjectid'>Your subjects: </label>";
        echo "<select name='subjectid' id='subjectid'>";
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $id = $row["subject_id"];
                $subject = $row["name"];
                echo "<option value='$id'>$subject</option>";
            }
        }
        echo "</select><br>";
        $conn->close();
    }

    function get_students() { 
        $conn = get_new_connection();
        $result = $conn->query("SELECT * FROM students");
        echo "<label for='studentid'>Students: </label>";
        echo "<select name='studentid' id='studentid'>";
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $id = $row["student_id"];
                $name = $row["first_name"]." ".$row["last_name"];
                echo "<option value='$id'>$name</option>";
            }
        }
        echo "</select><br>";
        $conn->close();
    }

    function get_content() {
        if ($_SERVER["REQUEST_METHOD"] !== "POST" && !isset($_SESSION["teacherid"])) {
            raise_login_err("Invalid request, either not post or the teacherid is not set!");
        }

        if ($_SERVER["REQUEST_METHOD"] === "POST") {
            if (!isset($_POST["username"]) || !isset($_POST["password"])) {
                raise_login_err("Invalid request body!");
            }
            $username = parse_input($_POST["username"]);
            $password = parse_input($_POST["password"]);

            $teacherid = get_teacher_by_username_and_password($username, $password);
            $_SESSION["teacherid"] = $teacherid;
        } else {
            $teacherid = $_SESSION["teacherid"];
            $username = get_teacher_by_id($teacherid);
        }

        
        echo "<fieldset><legend>$username</legend>";
        echo "<form action='saveGrades.php' method='post'>
                <label for='teacherid'>Your id: </label>
                <input name='teacherid' id='teacherid' value='$teacherid' readonly /><br/>";

        get_subjects($teacherid);
        get_students();
            
        echo "  <label for='grade'>Grade: </label>
                <input name='grade' id='grade' /><br>
                <input type='submit' value='Add grade' />
            </form>";
        echo "<a href='index.php'>Inapoi</a>";
        if (isset($_GET['gradeMsg'])) {
            echo '<p>'.$_GET['gradeMsg'].'</p>';
        }
        echo "</fieldset>";
    }
?>