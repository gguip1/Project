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
            $errMSG = "ID";
        }
        else if(empty($userPassword)){
            $errMSG = "password";
        }
        else if(empty($phoneNumber)){
            $errMSG = "phoneNumber";
        }
 
        if(!isset($errMSG)){
            try{
                $stmt = $con->prepare('INSERT INTO user(userID, userPassword, phoneNumber) VALUES(:userID, SHA1(:userPassword), :phoneNumber)');
                $stmt->bindParam(':userID', $userID);
                $stmt->bindParam(':userPassword', $userPassword);
                $stmt->bindParam(':phoneNumber', $phoneNumber);
 
                if($stmt->execute())
                {
                    $successMSG = "SUCCESS";
                }
                else
                {
                    $errMSG = "FAIL";
                }
 
            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }
 
    }
 
?>
 
<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;
 
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