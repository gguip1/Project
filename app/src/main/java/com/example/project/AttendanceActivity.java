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
    Handler handler;
    static int current_week;
    String attendanceData;
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
        /**  **/
        SimpleDateFormat currentDate = new SimpleDateFormat("HH", Locale.getDefault());
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        String currentDateTime = currentDate.format(now);
        int checkTime = Integer.parseInt(currentDateTime);

        current_week = cal.get(Calendar.WEEK_OF_YEAR) - 34;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        String user_ID = getIntent().getStringExtra("user_ID");
        String course_name = getIntent().getStringExtra("course_name");

        int attendance = 0;

        AccessDB task = new AccessDB(AttendanceActivity.this);

        try {
            attendanceData = task.execute("http://" + IP_ADDRESS + "/attendanceInfo.php", user_ID, course_name).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        TextView subjectText = (TextView) findViewById(R.id.subjectText);
        TextView currentClass = (TextView) findViewById(R.id.currentClass);
        TextView attendanceRate = (TextView) findViewById(R.id.attendance_rate);
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

        handler = new Handler();
        handler.post(updateDateTime);

        subjectText.setText("교과목명 : " + dataHashMap[0].get("attendance_name"));
        for(int i = 0; i < jsonParsing.getIndex(); i++){
            String attendance_check = dataHashMap[i].get("attendance_status");
            if(attendance_check.equals("1")){
                attendance_check = "출석";
                attendance++;
            }
            else if(attendance_check.equals("0") && Integer.parseInt(dataHashMap[i].get("attendance_week")) <= current_week)
                attendance_check = "결석";
            else
                attendance_check = "출결 등록 이전";
            itemList.add("강의주차 : " + dataHashMap[i].get("attendance_week") + "주차" + "\n수업 교시 :" + dataHashMap[i].get("attendance_time") + "교시" + "\n출석여부 : " + attendance_check);
        }

        int rate = attendance*100/jsonParsing.getIndex();
        attendanceRate.setText("출석률 : " + rate + "%    " + attendance + "/" + jsonParsing.getIndex()); // 출석률

//        Log.d("asdf", String.valueOf(current_week));
        currentClass.setText("현재 주차 : " + current_week + "주차");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,itemList);
        attendanceList.setAdapter(adapter);

        Button return_button = findViewById(R.id.return_button);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, MainActivity.class);
                AttendanceActivity.this.finish();
            }
        });
    }
}