package com.example.project.module;

import java.util.HashMap;

public class CurrentClass {
    private int currentTime;
    private int period;
    private String now_class;
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
    public CurrentClass(int currentTime,
                        HashMap<String, String> first,
                        HashMap<String, String> second,
                        HashMap<String, String> third,
                        HashMap<String, String> fourth,
                        HashMap<String, String> fifth,
                        HashMap<String, String> sixth,
                        HashMap<String, String> seventh,
                        HashMap<String, String> eighth,
                        HashMap<String, String> ninth,
                        HashMap<String, String> tenth){
        this.currentTime = currentTime;
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
        this.sixth = sixth;
        this.seventh = seventh;
        this.eighth = eighth;
        this.ninth = ninth;
        this.tenth = tenth;
    }
    public int getPeriod(){
        if (currentTime >= 9 && currentTime < 10) {
            period = 1;
        } else if (currentTime >= 10 && currentTime < 11) {
            period = 2;
        } else if (currentTime >= 11 && currentTime < 12) {
            period = 3;
        } else if (currentTime >= 12 && currentTime < 13) {
            period = 4;
        } else if (currentTime >= 13 && currentTime < 14) {
            period = 5;
        } else if (currentTime >= 14 && currentTime < 15) {
            period = 6;
        } else if (currentTime >= 15 && currentTime < 16) {
            period = 7;
        } else if (currentTime >= 16 && currentTime < 17) {
            period = 8;
        } else if (currentTime >= 17 && currentTime < 18) {
            period = 9;
        } else if (currentTime >= 18 && currentTime < 19) {
            period = 10;
        } else {
            period = 0;
        }
        return period;
    }

    public String getClass(int period, String dayWeek){
        if (period == 1){
            if(first.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = first.get(dayWeek);
            }
        }
        else if (period == 2){
            if(second.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = second.get(dayWeek);
            }
        }
        else if (period == 3){
            if(third.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = third.get(dayWeek);
            }
        }
        else if (period == 4){
            if(fourth.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = fourth.get(dayWeek);
            }
        }
        else if (period == 5){
            if(fifth.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = fifth.get(dayWeek);
            }
        }
        else if (period == 6){
            if(sixth.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = sixth.get(dayWeek);
            }
        }
        else if (period == 7){
            if(seventh.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = seventh.get(dayWeek);
            }
        }
        else if (period == 8){
            if(eighth.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = eighth.get(dayWeek);
            }
        }
        else if (period == 9){
            if(ninth.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = ninth.get(dayWeek);
            }
        }
        else if (period == 10){
            if(tenth.get(dayWeek).isEmpty()){
                now_class = "";
            }
            else{
                now_class = tenth.get(dayWeek);
            }
        }
        else{
            now_class = "";
        }
        return now_class;
    }
}
