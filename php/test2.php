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
                    // $userID = isset($_POST['userID']) ? $_POST['userID'] : $_GET['userID'];
                    // $userPassword = isset($_POST['userPassword']) ? $_POST['userPassword'] : $_GET['userPassword'];

                    $stmt = $con ->prepare('SELECT * FROM user_info WHERE user_id = :user_id');
                    $stmt->bindParam(':user_id', $userID);
                    $stmt->execute();

                    $list = $stmt->fetchAll(PDO::FETCH_ASSOC);

                    if($stmt->rowCount() == 0){
                        $errMSG = "로그인 정보를 찾을 수 없습니다.";
                        $stmt = $con->prepare('INSERT INTO user_info(user_id, user_pw, user_phone) VALUES(:user_id, :user_pw, :user_phone)');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->bindParam(':user_pw', $userPassword);
                        $stmt->bindParam(':user_phone', $phoneNumber);
                        $stmt->execute();
                    }
                    else if($stmt->rowCount() == 1){
                        $stmt = $con->prepare('SELECT * FROM user_info WHERE user_id = :user_id AND user_pw = :user_pw');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->bindParam(':user_pw', $userPassword);
                        $stmt->execute();
                        
                        if($stmt->rowCount() > 0){
                            $stmt = $con->prepare('SELECT * FROM timetable_info WHERE idx = :user_id');
                            $stmt->bindParam(':user_id', $userID);
                            $stmt->execute();
                            
                            if($stmt->rowCount() > 0){
                                $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
                            }
                            else{
                                $errMSG = "시간표 정보를 찾을 수 없습니다.";
                            }
                        }
                        else{
                            $errMSG = "비밀번호가 틀렸습니다.";
                        }
                    }
                    else{
                        $errMSG = "중복된 아이디가 데이터베이스에 존재합니다. 아이디가 중복된 모든 데이터를 삭제합니다.";
                        $stmt = $con->prepare('DELETE FROM user_info WHERE user_id = :user_id');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->execute();
                    }
                }
            } catch (PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }
 
    }
 
?>
 
<?php
    if (isset($errMSG)) echo json_encode($errMSG, JSON_UNESCAPED_UNICODE);
    // if (isset($list)) echo json_encode($list, JSON_UNESCAPED_UNICODE);  
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