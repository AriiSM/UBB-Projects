<?php
    $server = "localhost";
    $db = "lab6";
    
    if ($_SERVER["REQUEST_METHOD"] === "GET" && isset($_GET["departure"])) {
        $Statiile_Plecare = $_GET["departure"];
    } else if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["departure"])) {
        $Statiile_Plecare = $_POST["departure"];
    } else {
        die("Invalid request!");
    }

    $conn = new mysqli($server, "root", "", $db);
    if ($conn->connect_error) {
        die("Connection failed to MySQL ".$conn->connect_error);
    }
    
    $output = "";
    $sql = "SELECT DISTINCT Statiile_Sosire FROM `rute` WHERE Statiile_Plecare = \"$Statiile_Plecare\"";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $Statiile_Sosire = $row['Statiile_Sosire'];
            $output .= "<option value=\"$Statiile_Sosire\">$Statiile_Sosire</option>";
        }
    }
    $conn->close();

    echo $output;
?>
