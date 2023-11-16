package com.example.project;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.module.AccessDB;
import com.example.project.module.CurrentClass;
import com.example.project.module.JsonParsing;

import org.json.JSONException;
import org.w3c.dom.Text;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    /** 서버 IP_ADDRESS **/
    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    /** LOG - TAG **/
    private static String TAG = "DEBUG";
    /** 현재 수업 **/
    public static String now_class = "";
    /** 교시 **/
    static int period = 0;
    /** 요일 **/
    static int dayWeek;
    /** 올해의 현재 주차 **/
    static int week_of_year;
    /** 오늘 시간 **/
    static int hour_of_day;
    /** month **/
    static int day_of_month;
    /** month **/
    static int date;
    /** 현재 수업 TextView **/
    private TextView currentClass;
    /** 현재 수업과 비콘에서 사용하는 handler **/
    private TextView debug;
    static String user_ID;
    String resultData;
    String input_data;
    Handler handler;
    Button attendance_check;

    TextView resultMessage;
    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        attendance_check = view.findViewById(R.id.attendance_check);

        user_ID = this.getArguments().getString("user_ID"); /**  **/

        currentClass = view.findViewById(R.id.date);

        attendance_check.setOnClickListener(this);

        handler = new Handler();

        handler.post(updateDateTime);

        resultMessage = (TextView) view.findViewById(R.id.resultMessage);

        return view;
    }
    @Override
    public void onClick(View view) {
        String user_ID = this.getArguments().getString("user_ID");
        AccessDB task = new AccessDB(getContext());
//        Log.d("출석등록", user_ID + " : " + String.valueOf(week_of_year - 34) + " : " + String.valueOf(day_of_month) + String.valueOf(date) + " : " + String.valueOf(period) + " : " + now_class);
        try {
            resultData = task.execute("http://" + IP_ADDRESS + "/attendance.php", user_ID, String.valueOf(week_of_year - 34), String.valueOf(day_of_month) + String.valueOf(date), String.valueOf(period), now_class).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(resultData.equals(" \"sucess:1\"")){
            resultMessage.setText("해당 수업은 이미 출석이 완료되었습니다.");
        }
        else if(resultData.equals(" \"sucess:2\"")){
            resultMessage.setText("출석이 완료되었습니다.");
        }
        else if(resultData.equals(" \"fail\"")){
            resultMessage.setText("출석체크 실패");
        }
        else{
            resultMessage.setText(resultData);
        }
//        Log.d("resultData", resultData);
//        Log.d("resultData", now_class);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(updateDateTime);
    }
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

        dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        day_of_month = cal.get(Calendar.MONTH) + 1;
        week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
        hour_of_day = cal.get(Calendar.HOUR_OF_DAY);
        date = cal.get(Calendar.DATE);

//        Log.d("asdf", String.valueOf(week_of_year-34));
//        Log.d("checkTime_Debug", String.valueOf(checkTime));

        input_data = this.getArguments().getString("timetable_data");

        if(input_data.equals(" \"fail:3\"")){
            currentClass.setText("수업이 없습니다.");
        }
        else if(!input_data.isEmpty()){
            /** 입력된 JSON 형태의 String을 HashMap으로 변환 **/
            JsonParsing jsonParsing = new JsonParsing();
            String[] time_table_data = jsonParsing.parsingData(input_data);
            HashMap<String, String>[] timetablePeriod = new HashMap[jsonParsing.getIndex()];
            try {
                for(int i = 0; i < jsonParsing.getIndex(); i++){
                    timetablePeriod[i] = jsonParsing.paramMap(time_table_data[i]);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            /** 현재 수업 **/
            CurrentClass cc = new CurrentClass(checkTime, timetablePeriod);
            period = cc.getPeriod();
            switch (dayWeek){
                case 2:
                    now_class = cc.getClass(period, "mon");
                    if(now_class.isEmpty()){
                        currentClass.setText("현재 수업이 없습니다.");
                    }
                    else{
                        currentClass.setText(now_class);
                    }
                    break;
                case 3:
                    now_class = cc.getClass(period, "tue");
                    if(now_class.isEmpty()){
                        currentClass.setText("현재 수업이 없습니다.");
                    }
                    else{
                        currentClass.setText(now_class);
                    }
                    break;
                case 4:
                    now_class = cc.getClass(period, "wen");
                    if(now_class.isEmpty()){
                        currentClass.setText("현재 수업이 없습니다.");
                    }
                    else{
                        currentClass.setText(now_class);
                    }
                    break;
                case 5:
                    now_class = cc.getClass(period, "thu");
                    if(now_class.isEmpty()){
                        currentClass.setText("현재 수업이 없습니다.");
                    }
                    else{
                        currentClass.setText(now_class);
                    }
                    break;
                case 6:
                    now_class = cc.getClass(period, "fri");
                    if(now_class.isEmpty()){
                        currentClass.setText("현재 수업이 없습니다.");
                    }
                    else{
                        currentClass.setText(now_class);
                    }
                    break;
                default:
                    currentClass.setText("현재 수업이 없습니다.");
                    break;
            }


            Log.d(TAG, "현재 수업 : " + now_class + "현재 교시 : " + period);
        }
        else{
            Log.d(TAG, "input_data가 없습니다.");
        }
    }
}