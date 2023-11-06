<?php
    // attendance

    error_reporting(E_ALL);
    ini_set('display_errors',1);
 
    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $user_id=$_POST['user_id'];
        $course_name=$_POST['course_name'];
 
        if(empty($user_id)){
            $errMSG = "empty userID";
        }

        if (!isset($errMSG)) {
            try {
                if ((isset($_POST['user_id']) || isset($_GET['user_id'])) && (isset($_POST['course_name']) || isset($_GET['course_name']))) {
                    $user_id = isset($_POST['user_id']) ? $_POST['user_id'] : $_GET['user_id'];
                    $user_id = isset($_POST['course_name']) ? $_POST['course_name'] : $_GET['course_name'];
        
                    $stmt = $con->prepare('SELECT * FROM course_info WHERE user_id = :user_id AND course_name = :course_name');
                    $stmt->bindParam(':user_id', $user_id);
                    $stmt->bindParam(':course_name', $course_name);
                    $stmt->execute();

                    if ($stmt->rowCount() > 0) {
                        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
                    } else {
                        $errMSG = "FAIL";
                    }
                }
            } catch (PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }
 
    }
 
?>
 
<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;
    if (isset($result)) echo json_encode($result, JSON_UNESCAPED_UNICODE);  

        $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( !$android )
    {
?>
    <html>
       <body>
            <form action="<?php $_PHP_SELF ?>" method="POST">
                user_id: <input type = "text" name = "user_id" />
                course_name: <input type = "text" name = "course_name" />
                <input type = "submit" name = "submit" />
            </form>
 
       </body>
    </html>
<?php
    }
?>