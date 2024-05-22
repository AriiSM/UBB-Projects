<?php
$server = "localhost";
$db = "lab6";

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    die('Invalid request');
}

$conn = new mysqli($server, "root", "", $db);
if ($conn->connect_error) {
    die("Connection failed to MySQL " . $conn->connect_error);
}

$sql = "SELECT id FROM `student`";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $id = $row['id'];
        echo "<option value=\"$id\">$id</option>";
    }
}
$conn->close();
?>
