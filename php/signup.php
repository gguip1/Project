<?php
    // errMSG 정리
    // empty:1 = userID가 입력되지 않음
    // empty:2 = userPassword가 입력되지 않음
    // empty:3 = phoneNumber가 입력되지 않음
    // fail:1 = 데이터베이스에 일치하는 userID가 존재하지 않음
    // fail:2 = 데이터베이스에 일치하는 userID가 존재하지만 입력한 userPassword가 틀림
    // fail:21 = 데이터베이스에 일치하는 userID가 존재하지만 입력한 userPassword가 틀림 // Primary키의 중복
    // fail:3 = 데이터베이스에 중복된 아이디가 2개 이상일 경우 오류 데이터로 판단해서 해당하는 userID를 가지고 있는 모든 행을 제거
    // fail:4 = 입력한 userID와 일치하는 timetable_info의 데이터가 없음

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
            $errMSG = "empty:1";
        }
        else if(empty($userPassword)){
            $errMSG = "empty:2";
        }
        else if(empty($phoneNumber)){
            $errMSG = "empty:3";
        }

        if (!isset($errMSG)) {
            try {
                if ((isset($_POST['userID']) || isset($_GET['userID'])) && (isset($_POST['userPassword']) || isset($_GET['userPassword']))) {
                    // $userID = isset($_POST['userID']) ? $_POST['userID'] : $_GET['userID'];
                    // $userPassword = isset($_POST['userPassword']) ? $_POST['userPassword'] : $_GET['userPassword'];

                    $stmt = $con ->prepare('SELECT * FROM user_info WHERE user_id = :user_id');
                    $stmt->bindParam(':user_id', $userID);
                    $stmt->execute();

                    if($stmt->rowCount() == 0){ // 데이터베이스에 정보가 일치하는 userID가 존재하지 않는 경우
                        $errMSG = "fail:1";
                        $stmt = $con->prepare('INSERT INTO user_info(user_id, user_pw, user_phone) VALUES(:user_id, :user_pw, :user_phone)');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->bindParam(':user_pw', $userPassword);
                        $stmt->bindParam(':user_phone', $phoneNumber);
                        $stmt->execute();
                    }
                    else if($stmt->rowCount() == 1){ //데이터베이스에 정보가 일치하는 userID가 존재하는 경우 // 로그인 시도
                        $stmt = $con->prepare('SELECT * FROM user_info WHERE user_id = :user_id AND user_pw = :user_pw');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->bindParam(':user_pw', $userPassword);
                        $stmt->execute();
                        
                        if($stmt->rowCount() > 0){ // userID와 userPassword가 일치하는 경우 // 로그인 성공
                            $stmt = $con->prepare('SELECT * FROM timetable_info WHERE user_id = :user_id');
                            $stmt->bindParam(':user_id', $userID);
                            $stmt->execute();
                            
                            if($stmt->rowCount() > 0){ // userID와 일치하는 timetable의 값을 모두 불러옴
                                $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
                            }
                            else{ // userID와 일치하는 값이 없는 경우
                                $errMSG = "fail:3";
                            }
                        }
                        else{ // userID와 userPassword가 불일치하는 경우 // 로그인 실패
                            $errMSG = "fail:2";
                        }
                    }
                    else{ // 현재는 데이터베이스에 중복된 아이디가 2개 이상일 경우 오류 데이터로 판단해서 해당하는 userID를 가지고 있는 모든 행을 제거하는 중
                        $errMSG = "fail:4";
                        $stmt = $con->prepare('DELETE FROM user_info WHERE user_id = :user_id');
                        $stmt->bindParam(':user_id', $userID);
                        $stmt->execute();
                    }
                }
            } catch (PDOException $e) {
                // die("Database error: " . $e->getMessage());
                $errMSG = "fail:21"; // 현재는 PRIMARY KEY 중복을 고려하는 중
            }
        }
 
    }
 
?>
 
<?php
    if (isset($errMSG)) echo json_encode($errMSG, JSON_UNESCAPED_UNICODE); 
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