<?php
$server = "localhost";
$db = "lab6";

if ($_SERVER['REQUEST_METHOD'] !== 'GET' || !isset($_GET['id'])) {
    die('Invalid request');
}

$id = $_GET["id"];

$conn = new mysqli($server, "root", "", $db);
if ($conn->connect_error) {
    die("Connection failed to MySQL " . $conn->connect_error);
}

$sql = "SELECT * FROM `student` WHERE id = \"$id\"";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    if ($row = $result->fetch_assoc()) {
        $firstname = $row['prenume'];
        $lastname = $row['nume'];
        $phone = $row['telefon'];
        $email = $row['email'];
        echo "<label>Prenume: </label>";
        echo "<input type='text' name='prenume' value='$firstname' />";
        echo "<br>";
        echo "<label>Nume: </label>";
        echo "<input type='text' name='nume' value='$lastname' />";
        echo "<br>";
        echo "<label>Telefon: </label>";
        echo "<input type='number' name='telefon' value='$phone' />";
        echo "<br>";
        echo "<label>Email: </label>";
        echo "<input type='email' name='email' value='$email' />";
        echo "<br>";
        echo "<input id='saveBtn' type='submit' value='Update' disabled />";
    }
}
$conn->close();
?>
