<?php
    function parse_input($data) {
        return trim(htmlspecialchars(stripslashes($data)));
    }
    function get_new_connection() {
        $server = "localhost";
        $db = "lab6";

        $conn = new mysqli($server, "root", "", $db);
        if ($conn->connect_error) {
            die("Connection failed to MySQL ".$conn->connect_error);
        }
        return $conn;
    }
?>