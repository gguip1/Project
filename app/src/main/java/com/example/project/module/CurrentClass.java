package com.example.project.module;

import android.util.Log;

import java.util.HashMap;

public class CurrentClass {
    private int currentTime;
    private int period;
    private String now_class;

    private HashMap<String, String>[] timetablePeriod;

    public CurrentClass(int currentTime, HashMap<String, String>[] timetablePeriod){
        this.currentTime = currentTime;
        this.timetablePeriod = timetablePeriod;
    }
    public int getPeriod(){
        if (currentTime >= 9 && currentTime < 10 && timetablePeriod.length >= 1) {
            period = 1;
        } else if (currentTime >= 10 && currentTime < 11 && timetablePeriod.length >= 2) {
            period = 2;
        } else if (currentTime >= 11 && currentTime < 12 && timetablePeriod.length >= 3) {
            period = 3;
        } else if (currentTime >= 12 && currentTime < 13 && timetablePeriod.length >= 4) {
            period = 4;
        } else if (currentTime >= 13 && currentTime < 14 && timetablePeriod.length >= 5) {
            period = 5;
        } else if (currentTime >= 14 && currentTime < 15 && timetablePeriod.length >= 6) {
            period = 6;
        } else if (currentTime >= 15 && currentTime < 16 && timetablePeriod.length >= 7) {
            period = 7;
        } else if (currentTime >= 16 && currentTime < 17 && timetablePeriod.length >= 8) {
            period = 8;
        } else if (currentTime >= 17 && currentTime < 18 && timetablePeriod.length >= 9) {
            period = 9;
        } else if (currentTime >= 18 && currentTime < 19 && timetablePeriod.length >= 10) {
            period = 10;
        } else {
            period = 0;
        }
        return period;
    }

    public String getClass(int period, String dayWeek){
        if (period == 1 && timetablePeriod.length >= 1){
            if(timetablePeriod[0].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[0].get(dayWeek);
            }
        }
        else if (period == 2 && timetablePeriod.length >= 2){
            if(timetablePeriod[1].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[1].get(dayWeek);
            }
        }
        else if (period == 3 && timetablePeriod.length >= 3){
            if(timetablePeriod[2].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[2].get(dayWeek);
            }
        }
        else if (period == 4 && timetablePeriod.length >= 4){
            if(timetablePeriod[3].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[3].get(dayWeek);
            }
        }
        else if (period == 5 && timetablePeriod.length >= 5){
            if(timetablePeriod[4].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[4].get(dayWeek);
            }
        }
        else if (period == 6 && timetablePeriod.length >= 6){
            if(timetablePeriod[5].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[5].get(dayWeek);
            }
        }
        else if (period == 7 && timetablePeriod.length >= 7){
            if(timetablePeriod[6].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[6].get(dayWeek);
            }
        }
        else if (period == 8 && timetablePeriod.length >= 8){
            if(timetablePeriod[7].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[7].get(dayWeek);
            }
        }
        else if (period == 9 && timetablePeriod.length >= 9){
            if(timetablePeriod[8].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[8].get(dayWeek);
            }
        }
        else if (period == 10 && timetablePeriod.length >= 10){
            if(timetablePeriod[9].get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = timetablePeriod[9].get(dayWeek);
            }
        }
        else{
            now_class = "";
        }
        return now_class;
    }
}
