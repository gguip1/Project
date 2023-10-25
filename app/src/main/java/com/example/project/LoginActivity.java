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
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    //서버 주소
    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    // Log Tag
    private static String TAG = "phpsignup";
    private EditText editTextID;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // userPhoneNum을 가져오기 위한 권한 확인 절차
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS
            }, 121);
            return;
        }

        String userPhoneNum = telephonyManager.getLine1Number();
        editTextID = (EditText) findViewById(R.id.userID_Text);
        editTextPassword = (EditText) findViewById(R.id.password_Text);
        Button buttonInsert = (Button)findViewById(R.id.login_Button);

        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String userID = editTextID.getText().toString();
                String userPassword = editTextPassword.getText().toString();

                // 데이터베이스 접근
                AccessDB task = new AccessDB();
                task.execute("http://" + IP_ADDRESS + "/signup2.php", userID, userPassword, userPhoneNum);

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

            Log.d(TAG, result);
            String msg = "" + result;

            if (msg.equals(" 0") || msg.equals(" 1") || msg.equals(" 2")){ // editText에 입력이 없을 경우
                Toast.makeText(getApplicationContext(), "로그인 정보를 입력해주세요.", Toast.LENGTH_LONG).show();
            }
            else if(msg.equals(" FAIL")){  //로그인 실패할 경우
                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
            }
            else if(msg.substring(0, 8).equals(" SUCCESS")){ // 로그인 성공할 경우
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("dataFromServer", result.substring(8));
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
            }
            else if(msg.equals(" Can't access timetable_info")){ // 기타
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("dataFromServer", result);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
            }
            /** 1. String으로 들어오는 result를 HashMap으로 변환 고려 **/
            /** 2. 데이터를 가져올때 SUCCESS라는 성공 메시지를 포함해서 가져오게 됨. **/
            /** 2-1. SUCCESS를 찾기 위해 substring으로 쪼갰기 때문에 FAIL 같은 다른 값이 나올때 문자열의 문자가 8개가 안되기 때문에 오류가 뜸 그래서 else if를 FAIL 먼저 검사하게 함. (혹시라도 다른 값이 입력이 되면 문제가 있을 수 있음) **/
            /** 3. 데이터는 SUCCESS 이후를 잘라서 가져올 예정 (HashMap으로 변환 못했을 경우)**/

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