<?php
 
    error_reporting(E_ALL);
    ini_set('display_errors',1);
 
    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $userID=$_POST['로그인'];
        $userPassword=$_POST['비밀번호'];
        $phoneNumber=$_POST['고유번호'];
 
        if(empty($userID)){
            $errMSG = "로그인";
        }
        else if(empty($userPassword)){
            $errMSG = "비밀번호";
        }
        else if(empty($phoneNumber)){
            $errMSG = "고유번호";
        }

        if (!isset($errMSG)) {
            try {
                if ((isset($_POST['로그인']) || isset($_GET['로그인'])) && (isset($_POST['비밀번호']) || isset($_GET['비밀번호']))) {
                    $userID = isset($_POST['로그인']) ? $_POST['로그인'] : $_GET['로그인'];
                    $userPassword = isset($_POST['비밀번호']) ? $_POST['비밀번호'] : $_GET['비밀번호'];
        
                    $stmt = $con->prepare('SELECT * FROM user_info WHERE 로그인 = :로그인 AND 비밀번호 = SHA1(:비밀번호)');
                    $stmt->bindParam(':로그인', $userID);
                    $stmt->bindParam(':비밀번호', $userPassword);
                    $stmt->execute();

                    if ($stmt->rowCount() > 0) {
                        $successMSG = "SUCCESS";
                        $result = $stmt->fetch(PDO::FETCH_ASSOC);
                    } else {
                        $stmt = $con->prepare('INSERT INTO user_info(로그인, 비밀번호, 고유번호) VALUES(:로그인, SHA1(:비밀번호), :고유번호)');
                        $stmt->bindParam(':로그인', $userID);
                        $stmt->bindParam(':비밀번호', $userPassword);
                        $stmt->bindParam(':고유번호', $phoneNumber);
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
    if (isset($result)) echo json_encode($result);  

        $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( !$android )
    {
?>
    <html>
       <body>
            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID: <input type = "text" name = "로그인" />
                Password: <input type = "text" name = "비밀번호" />
                PhoneNumber: <input type = "text" name = "고유번호" />
                <input type = "submit" name = "submit" />
            </form>
 
       </body>
    </html>
<?php
    }
?>