<?php
    if ($_SERVER["REQUEST_METHOD"] !== "GET" || !isset($_GET["path"])) {
        die("Invalid request!");
    }
    $basedir = "D:\FACULTATE\SEM4\PW\Proiecte\installXAMPP\htdocs";
    $path = $basedir.'\\'.$_GET['path'];
    $childs = array();
    if (!is_dir($path)) {
        $myfile = fopen($path, "r") or die("Invalid file path!");
        echo fread($myfile, filesize($path));
        fclose($myfile);
    }
    else {
        $dir = new DirectoryIterator($path);
        foreach ($dir as $fileinfo) {
            if (!$fileinfo->isDot()) {
                array_push($childs, $fileinfo->getFilename());
            }
        }
        echo json_encode($childs);
    }
?>