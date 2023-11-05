package com.example.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, BeaconConsumer{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER/
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    // Log Tag
    private static String TAG = "DEBUG";
    private String mParam1;
    private String mParam2;
    /** 현재 수업 **/
    private static String now_class = "";
    /** 현재 수업 **/
    private TextView date;
    private Handler handler;

    static Context homeContext;

    /** 현재 시간 **/
    static int dayWeek;
    static int week_of_year;
    static int month;
    static int day;
    static int hour_of_day;
    static int minute;
    static int second;
    static int sClass;
    /** 현재 시간 **/

    /** 비콘 **/
    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    /** 비콘 **/

    public HomeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
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
            handler .postDelayed(this, 1000);
        }
    };

    private void updateCurrentDateTime() throws ParseException {
        SimpleDateFormat currentDate = new SimpleDateFormat("HH", Locale.getDefault());
        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        String currentDateTime = currentDate.format(now);
        dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour_of_day = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);

        int checkTime = Integer.parseInt(currentDateTime);

//        Log.d("dayWeek_Debug", String.valueOf(dayWeek));
//        Log.d("checkTime_Debug", String.valueOf(checkTime));

        String input_data = this.getArguments().getString("timetable_data");

        if(input_data.equals(" \"fail:3\"")){
            date.setText("수업이 없습니다.");
        }
        else {
            JsonParsing jsonParsing = new JsonParsing();
            String[] time_table_data = jsonParsing.parsingData(input_data);

            // 입력된 JSON 형태의 String을 HashMap으로 변환
            HashMap<String, String> first;
            HashMap<String, String> second;
            HashMap<String, String> third;
            HashMap<String, String> fourth;
            HashMap<String, String> fifth;
            HashMap<String, String> sixth;
            HashMap<String, String> seventh;
            HashMap<String, String> eighth;
            HashMap<String, String> ninth;
            HashMap<String, String> tenth;
            try {
                first = jsonParsing.paramMap(time_table_data[0]);
                second = jsonParsing.paramMap(time_table_data[1]);
                third = jsonParsing.paramMap(time_table_data[2]);
                fourth = jsonParsing.paramMap(time_table_data[3]);
                fifth = jsonParsing.paramMap(time_table_data[4]);
                sixth = jsonParsing.paramMap(time_table_data[5]);
                seventh = jsonParsing.paramMap(time_table_data[6]);
                eighth = jsonParsing.paramMap(time_table_data[7]);
                ninth = jsonParsing.paramMap(time_table_data[8]);
                tenth = jsonParsing.paramMap(time_table_data[9]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            switch (dayWeek) {
                case 1:
                case 7:
                    sClass = 0;
                    date.setText("수업이 없습니다.");
                    break;
                case 2:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 1;
                            now_class = first.get("mon");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 2;
                            now_class = second.get("mon");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("mon").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 3;
                            now_class = third.get("mon");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 4;
                            now_class = fourth.get("mon");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 5;
                            now_class = fifth.get("mon");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 6;
                            now_class = sixth.get("mon");
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 7;
                            now_class = seventh.get("mon");
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 8;
                            now_class = eighth.get("mon");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 9;
                            now_class = ninth.get("mon");
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 10;
                            now_class = tenth.get("mon");
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 3:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 1;
                            now_class = first.get("tue");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 2;
                            now_class = second.get("tue");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 3;
                            now_class = third.get("tue");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 4;
                            now_class = fourth.get("tue");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 5;
                            now_class = fifth.get("tue");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 6;
                            now_class = sixth.get("tue");
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 7;
                            now_class = seventh.get("tue");
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 8;
                            now_class = eighth.get("tue");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 9;
                            now_class = ninth.get("tue");
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 10;
                            now_class = tenth.get("tue");
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 4:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 1;
                            now_class = first.get("wen");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 2;
                            now_class = second.get("wen");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 3;
                            now_class = third.get("wen");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 4;
                            now_class = fourth.get("wen");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 5;
                            now_class = fifth.get("wen");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 6;
                            now_class = sixth.get("wen");
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 7;
                            now_class = seventh.get("wen");
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 8;
                            now_class = eighth.get("wen");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 9;
                            now_class = ninth.get("wen");
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 10;
                            now_class = tenth.get("wen");
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 5:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 1;
                            now_class = first.get("thu");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 2;
                            now_class = second.get("thu");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 3;
                            now_class = third.get("thu");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 4;
                            now_class = fourth.get("thu");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 5;
                            now_class = fifth.get("thu");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 6;
                            now_class = sixth.get("thu");
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 7;
                            now_class = seventh.get("thu");
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 8;
                            now_class = eighth.get("thu");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 9;
                            now_class = ninth.get("thu");
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 10;
                            now_class = tenth.get("thu");
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 6:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 1;
                            now_class = first.get("fri");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 2;
                            now_class = second.get("fri");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 3;
                            now_class = third.get("fri");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 4;
                            now_class = fourth.get("fri");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 5;
                            now_class = fifth.get("fri");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 6;
                            now_class = sixth.get("fri");
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 7;
                            now_class = seventh.get("fri");
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 8;
                            now_class = eighth.get("fri");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 9;
                            now_class = ninth.get("fri");
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            sClass = 10;
                            now_class = tenth.get("fri");
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + dayWeek);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button attendance_check = view.findViewById(R.id.attendance_check);

        String user_ID = this.getArguments().getString("user_ID");

        homeContext = container.getContext();

        date = view.findViewById(R.id.date);
        handler = new Handler() {
            public void handleMessage(Message msg) {

                // 비콘이 아무것도 없으면
                if(beaconList.isEmpty()){
                    attendance_check.setText("비콘없음");
                    attendance_check.setEnabled(false);
                    attendance_check.setBackgroundColor(Color.parseColor("#aaaaaa"));
                }
                // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
                for (Beacon beacon : beaconList) {
                    int major = beacon.getId2().toInt(); //beacon major

                    if(major == 4660){
                        //beacon 의 식별을 위하여 major값으로 확인
                        //이곳에 필요한 기능 구현
                        attendance_check.setText("버튼 활성화");
                        attendance_check.setEnabled(true);
                        attendance_check.setBackgroundColor(Color.parseColor("#f5c47e"));
                        // 비콘이 꺼져도 계속 활성화되어서 리스트를 초기화해서 다시 비콘이 있는지없는지 탐지하게 만듦
                        beaconList.clear();
                    }
                    else {
                        attendance_check.setText("비콘 아이디 틀림");
                        attendance_check.setEnabled(false);
                        attendance_check.setBackgroundColor(Color.parseColor("#aaaaaa"));
                    }
                    //textView.setText("ID : " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
                }

                // 자기 자신을 1초마다 호출
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        };

        handler.post(updateDateTime);

        attendance_check.setOnClickListener(this);

        /** 비콘 **/
        beaconManager = BeaconManager.getInstanceForApplication(container.getContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this); //?
        handler.sendEmptyMessage(0);

        return view;
    }
    @Override
    public void onClick(View view) {
        /** 더 고쳐야 함 **/
        String user_ID = this.getArguments().getString("user_ID");
        AccessDB task = new AccessDB();
        Log.d("debug", user_ID + " : " + String.valueOf(week_of_year - 34) + " : " +  String.valueOf(sClass)  + " : " +  String.valueOf(hour_of_day));

        /** 주말에는 sClass가 0이어서 empty:3 발생 **/
        task.execute("http://" + IP_ADDRESS + "/attendance.php", user_ID, String.valueOf(week_of_year - 34), String.valueOf(sClass), String.valueOf(hour_of_day));
//        task.execute("http://" + IP_ADDRESS + "/attendance.php", user_ID, String.valueOf(week_of_year - 34), "1", String.valueOf(hour_of_day));
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection connection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
        return false;
    }

    private class AccessDB extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(),
                    "잠시만 기다려 주세요.", null, true, true); /** progressDialog 디자인 수정 필요 **/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("debug", result);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String user_id = (String)params[1];
            String attendance_week = (String)params[2];
            String attendance_day = (String)params[3];
            String attendance_time = (String)params[4];
            String postParameters = "user_id=" + user_id + "&attendance_week=" + attendance_week + "&attendance_day=" + attendance_day + "&attendance_time=" + attendance_time;
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