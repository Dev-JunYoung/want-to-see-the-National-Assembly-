package com.example.team_project.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.team_project.Adapter.OpinionData;
import com.example.team_project.Adapter.RowData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreference {
    private final String TAG=this.getClass().getSimpleName();
    private final String bill_no = "BILL_NO";
    private static  SharedPreference instance=null;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String storage;
    Gson gson = new GsonBuilder().create();

    public SharedPreference(Context context) {
        sharedPreferences=context.getSharedPreferences("fav",0);
    }
    public SharedPreference(Context context,String storage) {
        Log.d(TAG, "SharedPreference: "+storage);
        sharedPreferences=context.getSharedPreferences("voting",0);
        this.storage=storage;
    }
    public static SharedPreference getInstance(Context context){
        if(instance==null){
            instance=new SharedPreference(context);
        }
        return instance;
    }
    public static SharedPreference getInstance(Context context, String storage){
        if(instance==null){
            instance=new SharedPreference(context,storage);
        }
        return instance;
    }
    public void setFavBill(RowData rowData){
        Log.d(TAG,"setFavBill");
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json = gson.toJson(rowData);
        editor.putString(rowData.getBill_no(),json);
        editor.apply();
    }
    public void setUnFavBill(RowData rowData){
        Log.d(TAG,"setUnFavBill");
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(rowData.getBill_no());
        editor.apply();
    }
    public RowData getFavBillState(String bill_no){
        String jsonCurrent=sharedPreferences.getString(bill_no,null);
        Type typeCurrent=new TypeToken<RowData>(){}.getType();
        RowData rowData=gson.fromJson(jsonCurrent,typeCurrent);
        return rowData;
    }
    public ArrayList<RowData> getFavList(){
        ArrayList<RowData> list=new ArrayList<>();
        String str= String.valueOf(sharedPreferences.getAll().values());
        try {
            JSONArray jsonArray=new JSONArray(str);
            Type typeCurrent=new TypeToken<RowData>(){}.getType();
            for(int i=0;i<jsonArray.length();i++){
                //RowData rowData=gson.fromJson(jsonArray.getString(i),typeCurrent);
                list.add(gson.fromJson(jsonArray.getString(i),typeCurrent));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
    public void setVoting(String bill_no, ArrayList<OpinionData> agreeList, ArrayList<OpinionData> disagreeList){
        Log.d(TAG,"setVoting"+storage);
        Log.d(TAG, "setVoting: "+context);
        SharedPreferences sharedPreferences=context.getSharedPreferences("voting",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("agree",agreeList);
            jsonObject.put("disagree",disagreeList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = gson.toJson(jsonObject);
        editor.putString(bill_no,json);
        editor.apply();
    }




}
