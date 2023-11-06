package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.module.SharedPreferencesManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //서버 주소
    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    // Log Tag
    private static String TAG = "DEBUG";
    private EditText editTextID;
    private EditText editTextPassword;
    private CheckBox checkBoxAutoLogin;

    String id;
    String password;
    static String userID;
    static String userPhoneNum;
    static String userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 어플리케이션 권한 확인 절차
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 121);
            return;
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        userPhoneNum = telephonyManager.getLine1Number();
        editTextID = (EditText) findViewById(R.id.userID_Text);
        editTextPassword = (EditText) findViewById(R.id.password_Text);
        Button buttonInsert = (Button)findViewById(R.id.login_Button);
        checkBoxAutoLogin=(CheckBox)findViewById(R.id.autologin_checkBox);

        // 전에 실행했을 때 자동로그인 체크박스값 가져오기
        boolean boo = SharedPreferencesManager.getBoolean(LoginActivity.this,"check");
        if(boo == true){ // 체크가 되어있었다면 아래 코드를 수행
            Map<String, String> loginInfo = SharedPreferencesManager.getLoginInfo(this);
            if (!loginInfo.isEmpty() && loginInfo.get("id") != ""){
                userID = loginInfo.get("id");
                userPassword = loginInfo.get("password");
                id = loginInfo.get("id");
                password = loginInfo.get("password");
                checkBoxAutoLogin.setChecked(true);
                // 데이터베이스 접근
                AccessDB task = new AccessDB();
                task.execute("http://" + IP_ADDRESS + "/signup.php", userID, userPassword, userPhoneNum);
            }
        }

        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                userID = editTextID.getText().toString();
                userPassword = editTextPassword.getText().toString();

                if (checkBoxAutoLogin.isChecked()) { // 체크박스 체크 되어 있으면
                    SharedPreferencesManager.setBoolean(LoginActivity.this, "check", checkBoxAutoLogin.isChecked()); //현재 체크박스 상태 값 저장
                } else { //체크박스가 해제되어있으면
                    SharedPreferencesManager.clearPreferences(LoginActivity.this); //로그인 정보를 모두 날림
                    SharedPreferencesManager.setBoolean(LoginActivity.this, "check", checkBoxAutoLogin.isChecked()); //현재 체크박스 상태 값 저장
                }

                // 데이터베이스 접근
                AccessDB time_table_data = new AccessDB();
                time_table_data.execute("http://" + IP_ADDRESS + "/signup.php", userID, userPassword, userPhoneNum);



                id = userID;
                password = userPassword;
                // 로그인 시도 이후 editText 초기화
                editTextID.setText("");
                editTextPassword.setText("");
            }
        });
    }

    private class AccessDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "잠시만 기다려 주세요.", null, true, true); /** progressDialog 디자인 수정 필요 **/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            Log.d(TAG, result);

            // errMSG 정리
            // empty:1 = userID가 입력되지 않음
            // empty:2 = userPassword가 입력되지 않음
            // empty:3 = phoneNumber가 입력되지 않음
            // fail:1 = 데이터베이스에 일치하는 userID가 존재하지 않음
            // fail:2 = 데이터베이스에 일치하는 userID가 존재하지만 입력한 userPassword가 틀림
            // fail:21 = 데이터베이스에 일치하는 userID가 존재하지만 입력한 userPassword가 틀림 // Primary키의 중복
            // fail:3 = 데이터베이스에 중복된 아이디가 2개 이상일 경우 오류 데이터로 판단해서 해당하는 userID를 가지고 있는 모든 행을 제거
            // fail:4 = 입력한 userID와 일치하는 timetable_info의 데이터가 없음

            if(result != null){
                if (result.equals(" \"empty:1\"") || result.equals(" \"empty:2\"") || result.equals(" \"empty:3\"")){ // editText에 입력이 없을 경우
                    Toast.makeText(getApplicationContext(), "로그인 정보를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" \"fail:1\"")){ // 데이터베이스에 로그인 정보가 등록되어 있지 않을때
                    Toast.makeText(getApplicationContext(), "로그인 정보가 틀렸습니다.", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" \"fail:2\"")){
                    Toast.makeText(getApplicationContext(), "잘못된 비밀번호", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" \"fail:21\"")){
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" \"fail:3\"")){
                    SharedPreferencesManager.setLoginInfo(LoginActivity.this, id ,password);
                    Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);

                    toMainActivity.putExtra("timetable_data", result); // 시간표 데이터 다음 액티비티에 전송

                    toMainActivity.putExtra("user_ID", userID); // userID 다음 액티비티에 전송

                    startActivity(toMainActivity);
                    finish();
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" \"fail:4\"")){  //로그인 실패할 경우
                    Toast.makeText(getApplicationContext(), "중복된 아이디가 발견되었습니다. 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferencesManager.setLoginInfo(LoginActivity.this, id ,password);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("timetable_data", result); // 시간표 데이터 다음 액티비티에 전송

                    intent.putExtra("user_ID", userID); // userID 다음 액티비티에 전송
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "서버와의 통신 오류", Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String userID = (String)params[1];
            String userPassword = (String)params[2];
            String userPhoneNum = (String)params[3];
            String postParameters = "userID=" + userID + "&userPassword=" + userPassword + "&userPhoneNum=" + userPhoneNum;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }
}