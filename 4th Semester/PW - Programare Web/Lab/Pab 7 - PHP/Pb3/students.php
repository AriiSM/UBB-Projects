<?php
    session_start();
    require_once('utils.php');
?>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student grades</title>
       <style>
        * {
            font-size: 20px;
            font-family: 'Roboto', sans-serif;
            color: black; /* Text alb */
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
            color: #black; 
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
            color: black; /* Text alb pentru placeholder */
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
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px; /* Margin-top adăugat pentru a separa tabelul de restul conținutului */
        }
        th, td {
            border: 1px solid #64B5F6; /* Bordură albastru deschis */
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #BBDEFB; /* Fundal albastru deschis */
        }
        tr:nth-child(even) {
            background-color: #f2f2f2; /* Fundal gri pentru rânduri pare */
        }
    </style>
</head>
<body>
    <fieldset>
        <legend><strong>Student grades</strong></legend>
        <table>
            <?php
                if (!isset($_GET["firstName"]) || !isset($_GET["lastName"])) {
                    redirect_to_main_page();
                }
                $firstname = parse_input($_GET["firstName"]);
                $lastname = parse_input($_GET["lastName"]);
                $conn = get_new_connection();
                $stmt = $conn->prepare("SELECT stud.student_id, subj.name, ss.grade, ss.submitted_at FROM students stud 
                                        INNER JOIN students_subjects ss ON stud.student_id = ss.student_id
                                        INNER JOIN subjects subj ON subj.subject_id = ss.subject_id 
                                        WHERE stud.first_name = ? AND stud.last_name = ?");
                $stmt->bind_param('ss', $firstname, $lastname);
                $stmt->execute();
                $result = $stmt->get_result();
                if ($result->num_rows == 0) {
                    raise_stud_err("Invalid student's first name / last name.");
                }
                echo "<tr><th>Student ID</th><th>Subject</th><th>Grade</th><th>Submitted at</th></tr>";
                while ($row = $result->fetch_assoc()) {
                    $id = $row['student_id'];
                    $subject = $row['name'];
                    $grade = $row['grade'];
                    $at = $row['submitted_at'];
                    echo "<tr><td>$id</td><td>$subject</td><td>$grade</td><td>$at</td></tr>";
                }
            ?>
        </table>
    </fieldset>  
</body>
</html>