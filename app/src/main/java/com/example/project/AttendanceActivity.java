package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project.module.AccessDB;
import com.example.project.module.CurrentClass;
import com.example.project.module.JsonParsing;
import com.example.project.module.SharedPreferencesManager;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AttendanceActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    private static String TAG = "DEBUG";
    int current_week;
    String attendanceData;
    String user_ID;
    String course_name;
    Handler handler;
    TextView currentClass;
    TextView subjectText;
    TextView attendanceRate;
    TextView totalRate;
    TextView current_attendance_status;
    ListView attendanceList;
    List<String> itemList;
    ProgressBar attendance_rate_bar; ProgressBar total_rate_bar; int rate; int trate;
    HashMap<String, String>[] dataHashMap; JsonParsing jsonParsing;
    private Runnable updateDateTime = new Runnable() {
        @Override
        public void run() {
            try {
                updateCurrentDateTime();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            handler.postDelayed(this, 1000);
        }
    };
    private void updateCurrentDateTime() throws ParseException {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        current_week = cal.get(Calendar.WEEK_OF_YEAR) - 34;
        currentClass.setText("현재 주차 : " + current_week + "주차");
        attendance_rate_bar.setProgress(rate);
        total_rate_bar.setProgress(trate);

        for(int i = 0; i < jsonParsing.getIndex(); i++) {
            if(current_week == Integer.parseInt(dataHashMap[i].get("attendance_week"))){
                String attendance_check = dataHashMap[i].get("attendance_status");
                if(attendance_check.equals("1")){
                    attendance_check = "출석";
                }
                else if(attendance_check.equals("0")){
                    attendance_check = "결석";
                }
                else if(attendance_check.equals("2")){
                    attendance_check = "출결 등록전";
                }
                else{
                    Log.d("버그", "출결 상태의 예외처리");
                }
                current_attendance_status.append( dataHashMap[i].get("attendance_week") + "주차   수업 일자 : " + dataHashMap[i].get("attendance_day")  + "  출석 여부 : " + attendance_check + "\n\n");
            }
            else if(i == 0){
                current_attendance_status.setText("");
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        handler = new Handler();
        handler.post(updateDateTime);

        user_ID = getIntent().getStringExtra("user_ID");
        course_name = getIntent().getStringExtra("course_name");

        AccessDB task = new AccessDB(AttendanceActivity.this);
        jsonParsing = new JsonParsing();

        try {
            attendanceData = task.execute("http://" + IP_ADDRESS + "/attendanceInfo.php", user_ID, course_name).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        currentClass = (TextView) findViewById(R.id.currentClass);
        subjectText = (TextView) findViewById(R.id.subjectText);
        attendanceRate = (TextView) findViewById(R.id.attendance_rate);
        totalRate = (TextView) findViewById(R.id.totalRate);
        current_attendance_status = (TextView) findViewById(R.id.current_attendance_status);
        attendanceList = findViewById(R.id.attendanceList);
        itemList = new ArrayList<String>();
        String[] data = jsonParsing.parsingData(attendanceData);
        dataHashMap = new HashMap[jsonParsing.getIndex()];

        try{
            for(int i = 0; i < jsonParsing.getIndex(); i++){
                dataHashMap[i] = jsonParsing.paramMap(data[i]);
            }
        }catch (JSONException e){
            throw new RuntimeException(e);
        }

        int attendance = 0;
        int nullAttendance = 0;
        subjectText.setText("교과목명 : " + dataHashMap[0].get("attendance_name"));
        for(int i = 0; i < jsonParsing.getIndex(); i++){
            String attendance_check = dataHashMap[i].get("attendance_status");
            if(attendance_check.equals("1")){
                attendance_check = "출석";
                attendance++;
            }
            else if(attendance_check.equals("0")){
                attendance_check = "결석";
            }
            else if(attendance_check.equals("2")){
                attendance_check = "출결 등록전";
                nullAttendance++;
            }
            else{
                Log.d("버그", "출결 상태의 예외처리");
            }
            itemList.add("강의주차 : " + dataHashMap[i].get("attendance_week") + "주차" + "   수업 교시 :" + dataHashMap[i].get("attendance_time") + "교시" + "\n수업 일자 :" + dataHashMap[i].get("attendance_day") + "   출석여부 : " + attendance_check);
        }
        rate = attendance*100/(jsonParsing.getIndex() - nullAttendance); // 출석률
        trate = (jsonParsing.getIndex() - nullAttendance)*100/jsonParsing.getIndex();
        attendanceRate.setText("출석률 : " + rate + "%    " + attendance + "/" + (jsonParsing.getIndex() - nullAttendance));
        totalRate.setText("수업진행률 : " + trate + "%");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.listview_layout,itemList);
        attendanceList.setAdapter(adapter);

        attendance_rate_bar = findViewById(R.id.attendance_rate_bar);
        attendance_rate_bar.setMax(100);
        total_rate_bar = findViewById(R.id.total_rate_bar);
        total_rate_bar.setMax(100);

        Button return_button = findViewById(R.id.return_button);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttendanceActivity.this.finish();
            }
        });
    }
}