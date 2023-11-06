package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.module.JsonParsing;
import com.example.project.module.SharedPreferencesManager;

import org.json.JSONException;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView student_department_text;
    TextView student_id_text;
    TextView student_grade_text;
    TextView student_name_text;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        /*** student_data가 없을때 고려필요 ***/

        JsonParsing jsonParsing = new JsonParsing();

        String student_data = this.getArguments().getString("student_data");
        String[] data = jsonParsing.parsingData(student_data);

        jsonParsing.parsingData(student_data);

        HashMap<String, String> info;
        try {
            info = jsonParsing.paramMap(data[0]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        student_department_text = (TextView) view.findViewById(R.id.student_department);
        student_id_text = (TextView) view.findViewById(R.id.student_id);
        student_grade_text = (TextView) view.findViewById(R.id.student_grade);
        student_name_text = (TextView) view.findViewById(R.id.student_name);

        student_department_text.setText("학과 : " + info.get("student_department"));
        student_id_text.setText("학번 : " + info.get("student_id"));
        student_grade_text.setText("학년 : " + info.get("student_grade"));
        student_name_text.setText("이름 : " + info.get("student_name"));

        Button logout = view.findViewById(R.id.logout_Button);
        logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        /**  **/
        SharedPreferencesManager.clearPreferences(getContext());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}