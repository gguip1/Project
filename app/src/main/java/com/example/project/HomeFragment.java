package com.example.project;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        String input_data = this.getArguments().getString("data");

        if(input_data.equals(" \"fail:3\"")){
            
        }
        else {
            JsonParsing jsonParsing = new JsonParsing();
            String[] data = jsonParsing.parsingData(input_data);

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
                first = jsonParsing.paramMap(data[0]);
                second = jsonParsing.paramMap(data[1]);
                third = jsonParsing.paramMap(data[2]);
                fourth = jsonParsing.paramMap(data[3]);
                fifth = jsonParsing.paramMap(data[4]);
                sixth = jsonParsing.paramMap(data[5]);
                seventh = jsonParsing.paramMap(data[6]);
                eighth = jsonParsing.paramMap(data[7]);
                ninth = jsonParsing.paramMap(data[8]);
                tenth = jsonParsing.paramMap(data[9]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            switch (dayWeek) {
                case 1:
                    date.setText("수업이 없습니다.");
                    break;
                case 2:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + first.get("mon"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + second.get("mon"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + third.get("mon"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fourth.get("mon"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fifth.get("mon"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + sixth.get("mon"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + seventh.get("mon"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + eighth.get("mon"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + ninth.get("mon"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("mon").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + tenth.get("mon"));
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 3:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + first.get("tue"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + second.get("tue"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + third.get("tue"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fourth.get("tue"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fifth.get("tue"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + sixth.get("tue"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + seventh.get("tue"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + eighth.get("tue"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + ninth.get("tue"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("tue").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + tenth.get("tue"));
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 4:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + first.get("wen"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + second.get("wen"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + third.get("wen"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fourth.get("wen"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fifth.get("wen"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + sixth.get("wen"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + seventh.get("wen"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + eighth.get("wen"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + ninth.get("wen"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("wen").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + tenth.get("wen"));
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 5:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + first.get("thu"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + second.get("thu"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + third.get("thu"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fourth.get("thu"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fifth.get("thu"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + sixth.get("thu"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + seventh.get("thu"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + eighth.get("thu"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + ninth.get("thu"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("thu").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + tenth.get("thu"));
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 6:
                    if (checkTime >= 9 && checkTime < 10) {
                        if(first.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + first.get("fri"));
                        }
                    } else if (checkTime >= 10 && checkTime < 11) {
                        if(second.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + second.get("fri"));
                        }
                    } else if (checkTime >= 11 && checkTime < 12) {
                        if(third.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + third.get("fri"));
                        }
                    } else if (checkTime >= 12 && checkTime < 13) {
                        if(fourth.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fourth.get("fri"));
                        }
                    } else if (checkTime >= 13 && checkTime < 14) {
                        if(fifth.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + fifth.get("fri"));
                        }
                    } else if (checkTime >= 14 && checkTime < 15) {
                        if(sixth.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + sixth.get("fri"));
                        }
                    } else if (checkTime >= 15 && checkTime < 16) {
                        if(seventh.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + seventh.get("fri"));
                        }
                    } else if (checkTime >= 16 && checkTime < 17) {
                        if(eighth.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + eighth.get("fri"));
                        }
                    } else if (checkTime >= 17 && checkTime < 18) {
                        if(ninth.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + ninth.get("fri"));
                        }
                    } else if (checkTime >= 18 && checkTime < 19) {
                        if(tenth.get("fri").isEmpty()){
                            date.setText("수업이 없습니다.");
                        }
                        else{
                            date.setText("현재 수업 : " + tenth.get("fri"));
                        }
                    } else {
                        date.setText("수업이 없습니다.");
                    }
                    break;
                case 7:
                    date.setText("수업이 없습니다.");
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