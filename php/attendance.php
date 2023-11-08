<?php
    // attendance_info에서 user_id, 주차, 교시, 출석 = true 을 검색해서 존재하면 이미 출석된 상태임을 반환
    // 존재하지 않으면 user_id, 주차, 교시, 출석 = true를 Insert 함.

    error_reporting(E_ALL);
    ini_set('display_errors',1);
 
    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
 
    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $user_id=$_POST['user_id'];
        $attendance_week=$_POST['attendance_week'];
        $attendance_day=$_POST['attendance_day'];
        $attendance_time=$_POST['attendance_time'];
        $attendance_status=1;
 
        if(empty($user_id)){
            $errMSG = "empty:1";
        }
        else if(empty($attendance_week)){
            $errMSG = "empty:2";
        }
        else if(empty($attendance_day)){
            $errMSG = "empty:3";
        }
        else if(empty($attendance_time)){
            $errMSG = "empty:4";
        }

        if (!isset($errMSG)) {
            try {
                if ((isset($_POST['user_id']))) {
                    $user_id = isset($_POST['user_id']) ? $_POST['user_id'] : $_GET['user_id'];
        
                    $stmt = $con->prepare('SELECT * FROM attendance_info WHERE user_id = :user_id AND attendance_week = :attendance_week AND attendance_time = :attendance_time AND attendance_status = :attendance_status');
                    $stmt->bindParam(':user_id', $user_id);
                    $stmt->bindParam(':attendance_week', $attendance_week);
                    $stmt->bindParam(':attendance_time', $attendance_time);
                    $stmt->bindParam(':attendance_status', $attendance_status);
                    $stmt->execute();

                    if ($stmt->rowCount() > 0) {
                        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
                    } else {
                        $stmt = $con->prepare('INSERT INTO attendance_info(user_id, attendance_week, attendance_day, attendance_time, attendance_status) VALUES(:user_id, :attendance_week, :attendance_day, :attendance_time, :attendance_status)');
                        $stmt->bindParam(':user_id', $user_id);
                        $stmt->bindParam(':attendance_week', $attendance_week);
                        $stmt->bindParam(':attendance_day', $attendance_day);
                        $stmt->bindParam(':attendance_time', $attendance_time);
                        $stmt->bindParam(':attendance_status', $attendance_status);
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
                attendance_week: <input type = "text" name = "attendance_week" />
                attendance_day: <input type = "text" name = "attendance_day" />
                attendance_time: <input type = "text" name = "attendance_time" />
                <input type = "submit" name = "submit" />
            </form>
 
       </body>
    </html>
<?php
    }
?>