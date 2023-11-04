package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    private static String TAG = "DEBUG";
    HomeFragment homeFragment;
    TimeTableFragment timeTableFragment;
    InfoFragment infoFragment;

    Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*** Data ***/

        String timetable_data = getIntent().getStringExtra("timetable_data");
        String user_ID = getIntent().getStringExtra("user_ID");

        AccessCourseInfo courseInfo = new AccessCourseInfo();
        courseInfo.execute("http://" + IP_ADDRESS + "/courseInfo.php", user_ID);

        AccessStudentInfo studentInfo = new AccessStudentInfo();
        studentInfo.execute("http://" + IP_ADDRESS + "/studentInfo.php", user_ID);

        bundle.putString("user_ID", user_ID);
        bundle.putString("timetable_data", timetable_data);

        Intent toAttendanceActivity = new Intent(MainActivity.this, AttendanceActivity.class);
        toAttendanceActivity.putExtra("user_ID", user_ID );

        /*** Fragment ***/

        final int[] before = {0};

        homeFragment = new HomeFragment();
        timeTableFragment = new TimeTableFragment();
        infoFragment = new InfoFragment();

        infoFragment.setArguments(bundle);
        timeTableFragment.setArguments(bundle);
        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        // 메인 액티비티 하단 네비게이션 바 화면 전환
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_left, R.anim.no_animation).replace(R.id.containers, homeFragment).commit();
                    before[0] = 0;
                    return true;
                } else if (item.getItemId() == R.id.timetable) {
                    if(before[0] == 0){
//                        Log.d("value", "0");
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_right, R.anim.no_animation).replace(R.id.containers, timeTableFragment).commit();
                    }
                    else if(before[0] == 1){
//                        Log.d("value", "1");
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_left, R.anim.no_animation).replace(R.id.containers, timeTableFragment).commit();
                    }
                    return true;
                } else if (item.getItemId() == R.id.info) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_right, R.anim.no_animation).replace(R.id.containers, infoFragment).commit();
                    before[0] = 1;
                    return true;
                }
                return false;
            }
        }
        );
    }
    private class AccessCourseInfo extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "잠시만 기다려 주세요.", null, true, true); /** progressDialog 디자인 수정 필요 **/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            bundle.putString("course_data", result);
            Log.d("result", result);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String user_id = (String)params[1];
            String postParameters = "user_id=" + user_id;
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

    private class AccessStudentInfo extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "잠시만 기다려 주세요.", null, true, true); /** progressDialog 디자인 수정 필요 **/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            bundle.putString("student_data", result.toString());
            Log.d("result", result);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String user_id = (String)params[1];
            String postParameters = "user_id=" + user_id;
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