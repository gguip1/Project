<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);
 
    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $userID=$_POST['userID'];
        $userPassword=$_POST['userPassword'];
        $phoneNumber=$_POST['userPhoneNum'];
 
        if(empty($userID)){
            $errMSG = "empty userID";
        }
        else if(empty($userPassword)){
            $errMSG = "empty userPassword";
        }
        else if(empty($phoneNumber)){
            $errMSG = "empty phoneNumber";
        }

        if (!isset($errMSG)) {
            try {
                if ((isset($_POST['userID']) || isset($_GET['userID'])) && (isset($_POST['userPassword']) || isset($_GET['userPassword']))) {
                    $userID = isset($_POST['userID']) ? $_POST['userID'] : $_GET['userID'];
                    $userPassword = isset($_POST['userPassword']) ? $_POST['userPassword'] : $_GET['userPassword'];
        
                    $stmt = $con->prepare('SELECT * FROM user_info WHERE user_id = :user_id AND user_pw = :user_pw');
                    $stmt->bindParam(':user_id', $userID);
                    $stmt->bindParam(':user_pw', $userPassword);
                    $stmt->execute();

                    $stmt_tt = $con->prepare('SELECT * FROM timetable_info WHERE user_id = :user_id');
                    $stmt_tt->bindParam(':user_id', $userID);
                    $stmt_tt->execute();

                    if ($stmt->rowCount() > 0) {
                        $successMSG = "SUCCESS";
                        $result = $stmt_tt->fetchAll(PDO::FETCH_ASSOC);
                    } else {
                        $stmt = $con->prepare('INSERT INTO user_info(user_id, user_pw, user_phone) VALUES(:user_id, :user_pw, :user_phone)');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->bindParam(':user_pw', $userPassword);
                        $stmt->bindParam(':user_phone', $phoneNumber);
                        $stmt->execute();
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
                ID: <input type = "text" name = "userID" />
                Password: <input type = "text" name = "userPassword" />
                PhoneNumber: <input type = "text" name = "userPhoneNum" />
                <input type = "submit" name = "submit" />
            </form>
 
       </body>
    </html>
<?php
    }
?>