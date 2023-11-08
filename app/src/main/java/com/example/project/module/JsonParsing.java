package com.example.project.module;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonParsing {
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private Iterator index;
    public String[] parsingData(String input_data){
        String[] result = new String[input_data.length()];
        try{
            jsonArray = new JSONArray(input_data);
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
        jsonObject = new JSONObject(String.valueOf(object));
        index = jsonObject.keys();
        while(index.hasNext()){
            String k = index.next().toString();
            hashmap.put(k, jsonObject.getString(k));
        }
        return hashmap;
    }
    public int getIndex(){
        return jsonArray.length();
    }
}
