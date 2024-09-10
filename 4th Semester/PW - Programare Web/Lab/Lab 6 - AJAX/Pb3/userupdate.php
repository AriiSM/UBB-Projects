<?php
$server = "localhost";
$db = "lab6";

if ($_SERVER['REQUEST_METHOD'] !== 'POST' || !isset($_POST['id']) || !isset($_POST['prenume'])
        || !isset($_POST['nume']) || !isset($_POST['telefon']) || !isset($_POST['email'])) {
    die('Invalid request');
}

$id = $_POST["id"];
$firstname = $_POST['prenume'];
$lastname = $_POST['nume'];
$phone = $_POST['telefon'];
$email = $_POST['email'];

$conn = new mysqli($server, "root", "", $db);
if ($conn->connect_error) {
    die("Connection failed to MySQL " . $conn->connect_error);
}

$sql = "UPDATE `student` SET prenume = \"$firstname\", nume = \"$lastname\", telefon = \"$phone\",
         email = \"$email\" WHERE id = \"$id\"";
if ($conn->query($sql) === TRUE) {
    echo "Record updated successfully!";
} else {
    echo "Error updating record: " . $conn->error;
}

$conn->close();
?>
