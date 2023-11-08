package com.example.project.module;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccessDB extends AsyncTask<String, Void, String> {

    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    private static String TAG = "DEBUG";
    ProgressDialog progressDialog;
    Context context;

    public AccessDB(Context context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                "잠시만 기다려 주세요.", null, true, true); /** progressDialog 디자인 수정 필요 **/
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }
    @Override
    protected String doInBackground(String... params) {
        String serverURL = (String)params[0];
        String user_id;
        String userPassword;
        String userPhoneNum;
        String attendance_name;
        String attendance_week;
        String attendance_day;
        String attendance_time;
        String postParameters;

        if(serverURL.equals("http://" + IP_ADDRESS + "/attendanceInfo.php")){
            user_id = (String)params[1];
            attendance_name = (String)params[2];
            postParameters = "user_id=" + user_id + "&attendance_name=" + attendance_name;
        }
        else if(serverURL.equals("http://" + IP_ADDRESS + "/signup.php")){
            user_id = (String)params[1];
            userPassword = (String)params[2];
            userPhoneNum = (String)params[3];
            postParameters = "userID=" + user_id + "&userPassword=" + userPassword + "&userPhoneNum=" + userPhoneNum;
        }
        else if(serverURL.equals("http://" + IP_ADDRESS + "/courseInfo.php")){
            user_id = (String)params[1];
            postParameters = "user_id=" + user_id;
        }
        else if(serverURL.equals("http://" + IP_ADDRESS + "/studentInfo.php")){
            user_id = (String)params[1];
            postParameters = "user_id=" + user_id;
        }
        else if(serverURL.equals("http://" + IP_ADDRESS + "/attendance.php")){
            user_id = (String)params[1];
            attendance_week = (String)params[2];
            attendance_day = (String)params[3];
            attendance_time = (String)params[4];
            attendance_name = (String)params[5];
            postParameters = "user_id=" + user_id + "&attendance_week=" + attendance_week + "&attendance_day=" + attendance_day + "&attendance_time=" + attendance_time + "&attendance_name=" + attendance_name;
        }
        else{
            postParameters = null;
        }

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

            Log.d("Access",sb.toString());

            return sb.toString();
        } catch (Exception e) {
            Log.d(TAG, "InsertData: Error ", e);
            return new String("Error: " + e.getMessage());
        }

    }
}