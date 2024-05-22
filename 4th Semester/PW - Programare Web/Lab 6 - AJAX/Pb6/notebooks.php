<?php
    $server = "localhost";
    $db = "lab6";

    $columns = array("producator", "procesor", "memorie", "capacitatehdd", "placavideo");
    $filterby = "";
    $filterval = "";
    if ($_SERVER["REQUEST_METHOD"] !== "GET") {
        die("Invalid request!");
    }
    if (isset($_GET["filterBy"]) && isset($_GET["filterValue"])) {
        $filterby = $_GET["filterBy"];
        $filterval = $_GET["filterValue"];
    }
    if ($filterby !== '' && !in_array($filterby, $columns)) {
        die("Invalid request!");
    }

    $conn = new mysqli($server, "root", "", $db);
    if ($conn->connect_error) {
        die("Connection failed to MySQL ".$conn->connect_error);
    }
    
    $sql = "SELECT * FROM `produs`";
    if ($filterby !== "") {
        $filterval = $conn->real_escape_string($filterval);
        if (is_numeric($filterval)) {
            $sql .= " WHERE $filterby = '$filterval'";
        } else {
            $sql .= " WHERE $filterby LIKE '%$filterval%'";
        }
    }
    $result = $conn->query($sql);

    echo "<tr><th>Producator</th><th>Procesor</th><th>Memorie</th><th>Capacitate HDD</th><th>Placa Video</th></tr>";
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $producator = $row['producator'];
            $procesor = $row['procesor'];
            $memorie = $row['memorie'];
            $capacitatehdd = $row['capacitatehdd'];
            $placavideo = $row['placavideo'];
            echo "<tr><td>$producator</td><td>$procesor</td><td>$memorie</td><td>$capacitatehdd</td><td>$placavideo</td></tr>";
        }
    }
    $conn->close();
?>
