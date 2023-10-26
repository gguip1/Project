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

//            Log.d("result", result);

            if(result != null){
                if (result.equals(" empty:1")){ // editText에 입력이 없을 경우
                    Toast.makeText(getApplicationContext(), "로그인 정보를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" fail:1")){  //로그인 실패할 경우
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                }
                else if(result.equals(" fail:2")){ // 기타
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("dataFromServer", result);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("dataFromServer", result);
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