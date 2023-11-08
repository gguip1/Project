package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.project.module.JsonParsing;

import org.json.JSONException;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String user_ID;
    TextView[][] t;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
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
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        String timetable_data = this.getArguments().getString("timetable_data");
        user_ID = this.getArguments().getString("user_ID");

        t = new TextView[10][5];

        //키 값을 입력해서 값을 보이면 됨
        t[0][0] = (TextView) view.findViewById(R.id.t00);
        t[0][1] = (TextView) view.findViewById(R.id.t01);
        t[0][2] = (TextView) view.findViewById(R.id.t02);
        t[0][3] = (TextView) view.findViewById(R.id.t03);
        t[0][4] = (TextView) view.findViewById(R.id.t04);
        t[1][0] = (TextView) view.findViewById(R.id.t10);
        t[1][1] = (TextView) view.findViewById(R.id.t11);
        t[1][2] = (TextView) view.findViewById(R.id.t12);
        t[1][3] = (TextView) view.findViewById(R.id.t13);
        t[1][4] = (TextView) view.findViewById(R.id.t14);
        t[2][0] = (TextView) view.findViewById(R.id.t20);
        t[2][1] = (TextView) view.findViewById(R.id.t21);
        t[2][2] = (TextView) view.findViewById(R.id.t22);
        t[2][3] = (TextView) view.findViewById(R.id.t23);
        t[2][4] = (TextView) view.findViewById(R.id.t24);
        t[3][0] = (TextView) view.findViewById(R.id.t30);
        t[3][1] = (TextView) view.findViewById(R.id.t31);
        t[3][2] = (TextView) view.findViewById(R.id.t32);
        t[3][3] = (TextView) view.findViewById(R.id.t33);
        t[3][4] = (TextView) view.findViewById(R.id.t34);
        t[4][0] = (TextView) view.findViewById(R.id.t40);
        t[4][1] = (TextView) view.findViewById(R.id.t41);
        t[4][2] = (TextView) view.findViewById(R.id.t42);
        t[4][3] = (TextView) view.findViewById(R.id.t43);
        t[4][4] = (TextView) view.findViewById(R.id.t44);
        t[5][0] = (TextView) view.findViewById(R.id.t50);
        t[5][1] = (TextView) view.findViewById(R.id.t51);
        t[5][2] = (TextView) view.findViewById(R.id.t52);
        t[5][3] = (TextView) view.findViewById(R.id.t53);
        t[5][4] = (TextView) view.findViewById(R.id.t54);
        t[6][0] = (TextView) view.findViewById(R.id.t60);
        t[6][1] = (TextView) view.findViewById(R.id.t61);
        t[6][2] = (TextView) view.findViewById(R.id.t62);
        t[6][3] = (TextView) view.findViewById(R.id.t63);
        t[6][4] = (TextView) view.findViewById(R.id.t64);
        t[7][0] = (TextView) view.findViewById(R.id.t70);
        t[7][1] = (TextView) view.findViewById(R.id.t71);
        t[7][2] = (TextView) view.findViewById(R.id.t72);
        t[7][3] = (TextView) view.findViewById(R.id.t73);
        t[7][4] = (TextView) view.findViewById(R.id.t74);
        t[8][0] = (TextView) view.findViewById(R.id.t80);
        t[8][1] = (TextView) view.findViewById(R.id.t81);
        t[8][2] = (TextView) view.findViewById(R.id.t82);
        t[8][3] = (TextView) view.findViewById(R.id.t83);
        t[8][4] = (TextView) view.findViewById(R.id.t84);
        t[9][0] = (TextView) view.findViewById(R.id.t90);
        t[9][1] = (TextView) view.findViewById(R.id.t91);
        t[9][2] = (TextView) view.findViewById(R.id.t92);
        t[9][3] = (TextView) view.findViewById(R.id.t93);
        t[9][4] = (TextView) view.findViewById(R.id.t94);

        if(timetable_data.equals(" \"fail:3\"")){
            for(int i = 0; i <= 9; i++){
                for(int j = 0; j <= 4; j++){
                }
            }
        }
        else{ /** 시간표 테이블에 표시 **/
            JsonParsing jsonParsing = new JsonParsing();
            String[] data = jsonParsing.parsingData(timetable_data);
            // 입력된 JSON 형태의 String을 HashMap으로 변환
            HashMap<String, String>[] timetablePeriod = new HashMap[jsonParsing.getIndex()];
            try {
                for(int i = 0; i < jsonParsing.getIndex(); i++){
                    timetablePeriod[i] = jsonParsing.paramMap(data[i]);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            for(int j = 0; j < jsonParsing.getIndex(); j++){
                    t[j][0].setText(timetablePeriod[j].get("mon")); t[j][1].setText(timetablePeriod[j].get("tue")); t[j][2].setText(timetablePeriod[j].get("wen")); t[j][3].setText(timetablePeriod[j].get("thu")); t[j][4].setText(timetablePeriod[j].get("fri"));
            }
        }

        //시간표에 과목이 등록된 셀만 클릭이벤트 발생
        for(int i = 0; i <= 9; i++){
            for(int j = 0; j <= 4; j++){
                if(t[i][j].getText().toString().isEmpty() || t[i][j].getText().toString().equals("시간표 데이터를 가져올 수 없습니다.")){}
                else{
                    t[i][j].setOnClickListener(this);
                }
            }
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        /**  **/
        Intent toAttendanceActivity = new Intent(getActivity(), AttendanceActivity.class);
        toAttendanceActivity.putExtra("course_name", ((TextView)view).getText().toString());
        toAttendanceActivity.putExtra("user_ID", user_ID);
        startActivity(toAttendanceActivity);
    }
}