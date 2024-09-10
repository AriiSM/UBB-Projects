<?php
    $server = "localhost";
    $db = "lab6";
    
    if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
        die('Invalid request');    
    }
    
    $conn = new mysqli($server, "root", "", $db);
    if ($conn->connect_error) {
        die("Connection failed to MySQL ".$conn->connect_error);
    }

    $output = "";
    $sql = "SELECT DISTINCT Statiile_Plecare FROM `rute`";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $Statiile_Plecare = $row['Statiile_Plecare'];
            $output .= "<option value=\"$Statiile_Plecare\">$Statiile_Plecare</option>";
        }
    }
    $conn->close();

    echo $output;
?>
