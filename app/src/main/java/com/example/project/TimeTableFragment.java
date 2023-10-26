package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        TextView err_msg = (TextView) view.findViewById(R.id.timetable_Title);
        String input_data = this.getArguments().getString("data");
        if(input_data.equals(" fail:2")){
            err_msg.setText("시간표 데이터를 가져올 수 없습니다.");
        }
        else{
            String[] data = JsonParsing(input_data);

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
                first = paramMap(data[0]);
                second = paramMap(data[1]);
                third = paramMap(data[2]);
                fourth = paramMap(data[3]);
                fifth = paramMap(data[4]);
                sixth = paramMap(data[5]);
                seventh = paramMap(data[6]);
                eighth = paramMap(data[7]);
                ninth = paramMap(data[7]);
                tenth = paramMap(data[7]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            //키 값을 입력해서 값을 보이면 됨
            TextView t00 = (TextView) view.findViewById(R.id.t00);
            TextView t01 = (TextView) view.findViewById(R.id.t01);
            TextView t02 = (TextView) view.findViewById(R.id.t02);
            TextView t03 = (TextView) view.findViewById(R.id.t03);
            TextView t04 = (TextView) view.findViewById(R.id.t04);
            TextView t10 = (TextView) view.findViewById(R.id.t10);
            TextView t11 = (TextView) view.findViewById(R.id.t11);
            TextView t12 = (TextView) view.findViewById(R.id.t12);
            TextView t13 = (TextView) view.findViewById(R.id.t13);
            TextView t14 = (TextView) view.findViewById(R.id.t14);
            TextView t20 = (TextView) view.findViewById(R.id.t20);
            TextView t21 = (TextView) view.findViewById(R.id.t21);
            TextView t22 = (TextView) view.findViewById(R.id.t22);
            TextView t23 = (TextView) view.findViewById(R.id.t23);
            TextView t24 = (TextView) view.findViewById(R.id.t24);
            TextView t30 = (TextView) view.findViewById(R.id.t30);
            TextView t31 = (TextView) view.findViewById(R.id.t31);
            TextView t32 = (TextView) view.findViewById(R.id.t32);
            TextView t33 = (TextView) view.findViewById(R.id.t33);
            TextView t34 = (TextView) view.findViewById(R.id.t34);
            TextView t40 = (TextView) view.findViewById(R.id.t40);
            TextView t41 = (TextView) view.findViewById(R.id.t41);
            TextView t42 = (TextView) view.findViewById(R.id.t42);
            TextView t43 = (TextView) view.findViewById(R.id.t43);
            TextView t44 = (TextView) view.findViewById(R.id.t44);
            TextView t50 = (TextView) view.findViewById(R.id.t50);
            TextView t51 = (TextView) view.findViewById(R.id.t51);
            TextView t52 = (TextView) view.findViewById(R.id.t52);
            TextView t53 = (TextView) view.findViewById(R.id.t53);
            TextView t54 = (TextView) view.findViewById(R.id.t54);
            TextView t60 = (TextView) view.findViewById(R.id.t60);
            TextView t61 = (TextView) view.findViewById(R.id.t61);
            TextView t62 = (TextView) view.findViewById(R.id.t62);
            TextView t63 = (TextView) view.findViewById(R.id.t63);
            TextView t64 = (TextView) view.findViewById(R.id.t64);
            TextView t70 = (TextView) view.findViewById(R.id.t70);
            TextView t71 = (TextView) view.findViewById(R.id.t71);
            TextView t72 = (TextView) view.findViewById(R.id.t72);
            TextView t73 = (TextView) view.findViewById(R.id.t73);
            TextView t74 = (TextView) view.findViewById(R.id.t74);


            t00.setText(first.get("mon")); t01.setText(first.get("tue")); t02.setText(first.get("wen")); t03.setText(first.get("thu")); t04.setText(first.get("fri"));
            t10.setText(second.get("mon")); t11.setText(second.get("tue")); t12.setText(second.get("wen")); t13.setText(second.get("thu")); t14.setText(second.get("fri"));
            t20.setText(third.get("mon")); t21.setText(third.get("tue")); t22.setText(third.get("wen")); t23.setText(third.get("thu")); t24.setText(third.get("fri"));
            t30.setText(fourth.get("mon")); t31.setText(fourth.get("tue")); t32.setText(fourth.get("wen")); t33.setText(fourth.get("thu")); t34.setText(fourth.get("fri"));
            t40.setText(fifth.get("mon")); t41.setText(fifth.get("tue")); t42.setText(fifth.get("wen")); t43.setText(fifth.get("thu")); t44.setText(fifth.get("fri"));
            t50.setText(sixth.get("mon")); t51.setText(sixth.get("tue")); t52.setText(sixth.get("wen")); t53.setText(sixth.get("thu")); t54.setText(sixth.get("fri"));
            t60.setText(seventh.get("mon")); t61.setText(seventh.get("tue")); t62.setText(seventh.get("wen")); t63.setText(seventh.get("thu")); t64.setText(seventh.get("fri"));
            t70.setText(eighth.get("mon")); t71.setText(eighth.get("tue")); t72.setText(eighth.get("wen")); t73.setText(eighth.get("thu")); t74.setText(eighth.get("fri"));
        }
        // Inflate the layout for this fragment
        return view;
    }

    public String[] JsonParsing(String data){
        String[] result = new String[data.length()];
        try{
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                result[i] = String.valueOf(jsonObject);
            }
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, String> paramMap(Object object) throws JSONException {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        JSONObject json = new JSONObject(String.valueOf(object));
        Iterator i = json.keys();
        while(i.hasNext()){
            String k = i.next().toString();
            hashmap.put(k, json.getString(k));
        }
        return hashmap;
    }
}