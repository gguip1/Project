package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project.module.AccessDB;
import com.example.project.module.JsonParsing;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AttendanceActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    private static String TAG = "DEBUG";
    String attendanceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        String user_ID = getIntent().getStringExtra("user_ID");
        String course_name = getIntent().getStringExtra("course_name");

        AccessDB task = new AccessDB(AttendanceActivity.this);

        try {
            attendanceData = task.execute("http://" + IP_ADDRESS + "/attendanceInfo.php", user_ID, course_name).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        TextView subjectText = (TextView) findViewById(R.id.subjectText);
        ListView attendanceList = findViewById(R.id.attendanceList);
        List<String> itemList = new ArrayList<String>();
        JsonParsing jsonParsing = new JsonParsing();
        String[] data = jsonParsing.parsingData(attendanceData);
        HashMap<String, String>[] dataHashMap = new HashMap[jsonParsing.getIndex()];
        try{
            for(int i = 0; i < jsonParsing.getIndex(); i++){
                dataHashMap[i] = jsonParsing.paramMap(data[i]);
            }
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
        subjectText.setText("교과목명 : " + dataHashMap[0].get("attendance_name"));
        for(int i = 0; i < jsonParsing.getIndex(); i++){
            itemList.add("강의주차 : " + dataHashMap[i].get("attendance_week") + "주차" + "\n수업 교시 :" + dataHashMap[i].get("attendance_time") + "교시" + "\n출석여부 : " + dataHashMap[i].get("attendance_status"));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,itemList);
        attendanceList.setAdapter(adapter);
    }
}