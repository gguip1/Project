package com.example.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class JsonParsing {
    public String[] parsingData(String data){
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
