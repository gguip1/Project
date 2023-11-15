package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.MenuItem;

import com.example.project.module.AccessDB;
import com.google.android.material.navigation.NavigationBarView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    HomeFragment homeFragment;
    TimeTableFragment timeTableFragment;
    InfoFragment infoFragment;
    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    private static String IP_ADDRESS = "rldjqdus05.cafe24.com";
    private static String TAG = "DEBUG";
    private static String user_ID;
    Bundle bundle = new Bundle();

    String courseInfoData;
    String studentInfoData;
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                        //비콘 추가
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*** Data ***/

        String timetable_data = getIntent().getStringExtra("timetable_data");
        user_ID = getIntent().getStringExtra("user_ID");

        AccessDB courseInfo = new AccessDB(MainActivity.this);
        AccessDB studentInfo = new AccessDB(MainActivity.this);
        try {
            courseInfoData = courseInfo.execute("http://" + IP_ADDRESS + "/courseInfo.php", user_ID).get();
            studentInfoData = studentInfo.execute("http://" + IP_ADDRESS + "/studentInfo.php", user_ID).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        bundle.putString("course_data", courseInfoData);
        bundle.putString("user_ID", user_ID);
        bundle.putString("timetable_data", timetable_data);
        bundle.putString("student_data", studentInfoData);

        /** Beacon **/
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        handler.sendEmptyMessage(0);

        /*** Fragment ***/

        final int[] before = {0};

        homeFragment = new HomeFragment();
        timeTableFragment = new TimeTableFragment();
        infoFragment = new InfoFragment();

        infoFragment.setArguments(bundle);
        timeTableFragment.setArguments(bundle);
        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        // 메인 액티비티 하단 네비게이션 바 화면 전환
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_left, R.anim.no_animation).replace(R.id.containers, homeFragment).commit();
                    before[0] = 0;
                    return true;
                } else if (item.getItemId() == R.id.timetable) {
                    if(before[0] == 0){
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, timeTableFragment).commit();
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_right, R.anim.no_animation).replace(R.id.containers, timeTableFragment).commit();
                    }
                    else if(before[0] == 1){
//                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, timeTableFragment).commit();
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_left, R.anim.no_animation).replace(R.id.containers, timeTableFragment).commit();
                    }
                    return true;
                } else if (item.getItemId() == R.id.info) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, infoFragment).commit();
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_right, R.anim.no_animation).replace(R.id.containers, infoFragment).commit();
                    before[0] = 1;
                    return true;
                }
                return false;
            }
        }
        );
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 비콘이 아무것도 없으면
//            if(beaconList.isEmpty() || homeFragment.now_class.isEmpty()){
            if(beaconList.isEmpty()){
                homeFragment.attendance_check.setEnabled(false);
                homeFragment.attendance_check.setTextColor(Color.parseColor("#000000"));
                homeFragment.attendance_check.setText("비활성화");
                homeFragment.attendance_check.setBackgroundResource(R.drawable.button_off_home_frag);
//                homeFragment.attendance_check.setBackgroundColor(Color.parseColor("#F6F4FB"));
            }
            else{
                // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
                for (Beacon beacon : beaconList) {
                    int major = beacon.getId2().toInt(); //beacon major
                    if(major == 4660){
                        homeFragment.attendance_check.setEnabled(true);
                        homeFragment.attendance_check.setTextColor(Color.parseColor("#FFFFFF"));
                        homeFragment.attendance_check.setText("출석체크");
                        homeFragment.attendance_check.setBackgroundResource(R.drawable.button_on_home_frag);
//                        homeFragment.attendance_check.setBackgroundColor(Color.parseColor("#618EFF"));
                        beaconList.clear();
                    }
                    else {
                        homeFragment.attendance_check.setEnabled(false);
                        homeFragment.attendance_check.setBackgroundColor(Color.parseColor("#aaaaaa"));
                    }
                    //textView.setText("ID : " + beacon.getId2() + " / " + "Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
                }
            }
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
}