package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String now_class = "";

    private TextView date;
    private Handler handler;

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
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);

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
                    date.setText("수업이 없습니다.");
                    break;
                case 2:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = first.get("mon");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = second.get("mon");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("mon").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = third.get("mon");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fourth.get("mon");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get("mon");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = sixth.get("mon");
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = seventh.get("mon");
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = eighth.get("mon");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = ninth.get("mon");
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("mon").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
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
                            now_class = first.get("tue");
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = second.get("tue");
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = third.get("tue");
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fourth.get("tue");
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get("tue");
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + sixth.get("tue"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(seventh.get("tue"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(eighth.get("tue"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(ninth.get("tue"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("tue").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(tenth.get("tue"));
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
                            now_class = fifth.get(first.get("wen"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(second.get("wen"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(third.get("wen"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(fourth.get("wen"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(fifth.get("wen"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(sixth.get("wen"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(seventh.get("wen"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(eighth.get("wen"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(ninth.get("wen"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("wen").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(tenth.get("wen"));
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
                            now_class = fifth.get(first.get("thu"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(second.get("thu"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(third.get("thu"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(fourth.get("thu"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(fifth.get("thu"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(sixth.get("thu"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(seventh.get("thu"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(!eighth.get("thu").isEmpty()){
                            now_class = fifth.get(eighth.get("thu"));
                        }
                        else{
                            date.setText("수업이 없습니다.");
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(ninth.get("thu"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("thu").equals(" ")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(tenth.get("thu"));
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
                            now_class = fifth.get(first.get("fri"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(second.get("fri"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(third.get("fri"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(fourth.get("fri"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(fifth.get("fri"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(sixth.get("fri"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(seventh.get("fri"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(eighth.get("fri"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(ninth.get("fri"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("fri").equals("")){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            now_class = fifth.get(tenth.get("fri"));
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

        attendance_check.setOnClickListener(this);

        date = view.findViewById(R.id.date);
        handler = new Handler();
        handler.post(updateDateTime);

        return view;
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AttendanceActivity.class);
        startActivity(intent);
    }
}