package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    TimeTableFragment timeTableFragment;
    InfoFragment infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] before = {0};

        Bundle bundle = new Bundle();
        Intent secondIntent = getIntent();
        String msg = secondIntent.getStringExtra("dataFromServer");
        Log.d("msg", msg);
        bundle.putString("msg", msg);

        homeFragment = new HomeFragment();
        timeTableFragment = new TimeTableFragment();
        infoFragment = new InfoFragment();

        timeTableFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_left, R.anim.no_animation).replace(R.id.containers, homeFragment).commit();
                    before[0] = 0;
                    return true;
                } else if (item.getItemId() == R.id.setting) {
                    if(before[0] == 0){
                        Log.d("value", "0");
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_right, R.anim.no_animation).replace(R.id.containers, timeTableFragment).commit();
                    }
                    else if(before[0] == 1){
                        Log.d("value", "1");
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_left, R.anim.no_animation).replace(R.id.containers, timeTableFragment).commit();
                    }
                    return true;
                } else if (item.getItemId() == R.id.info) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.to_right, R.anim.no_animation).replace(R.id.containers, infoFragment).commit();
                    before[0] = 1;
                    return true;
                }
                return false;
            }
        });

    }
}