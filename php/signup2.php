<?php
 
    error_reporting(E_ALL);
    ini_set('display_errors',1);
 
    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $userID=$_POST['userID'];
        $userPassword=$_POST['userPassword'];
        $phoneNumber=$_POST['phoneNumber'];
 
        if(empty($userID)){
            $errMSG = "0";
        }
        else if(empty($userPassword)){
            $errMSG = "password";
        }
        else if(empty($phoneNumber)){
            $errMSG = "phoneNumber";
        }

        if (!isset($errMSG)) {
            try {
                if ((isset($_POST['userID']) || isset($_GET['userID'])) && (isset($_POST['userPassword']) || isset($_GET['userPassword']))) {
                    $userID = isset($_POST['userID']) ? $_POST['userID'] : $_GET['userID'];
                    $userPassword = isset($_POST['userPassword']) ? $_POST['userPassword'] : $_GET['userPassword'];
        
                    $stmt = $con->prepare('SELECT * FROM user WHERE userID = :userID AND userPassword = SHA1(:userPassword)');
                    $stmt->bindParam(':userID', $userID);
                    $stmt->bindParam(':userPassword', $userPassword);
                    $stmt->execute();

                    if ($stmt->rowCount() > 0) {
                        $successMSG = "SUCCESS";
                        $result = $stmt->fetch(PDO::FETCH_ASSOC);
                    } else {
                        $stmt = $con->prepare('INSERT INTO user(userID, userPassword, phoneNumber) VALUES(:userID, SHA1(:userPassword), :phoneNumber)');
                        $stmt->bindParam(':userID', $userID);
                        $stmt->bindParam(':userPassword', $userPassword);
                        $stmt->bindParam(':phoneNumber', $phoneNumber);
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
                ID: <input type = "text" name = "userID" />
                Password: <input type = "text" name = "userPassword" />
                PhoneNumber: <input type = "text" name = "phoneNumber" />
                <input type = "submit" name = "submit" />
            </form>
 
       </body>
    </html>
<?php
    }
?>