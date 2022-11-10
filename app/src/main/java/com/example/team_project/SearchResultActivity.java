package com.example.team_project;

import static com.example.team_project.Retrofit.RetrofitClient.KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.team_project.Adapter.RowAdapter;
import com.example.team_project.Adapter.RowData;
import com.example.team_project.Retrofit.RetrofitClient;
import com.example.team_project.databinding.ActivitySearchResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {
    private final String TAG=this.getClass().getSimpleName();
    private ActivitySearchResultBinding binding;
    private String KEYWORD;
    private RowAdapter rowAdapter;
    private RecyclerView recyclerView;
    private ArrayList<RowData> rowList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView=binding.rv;
        KEYWORD=getIntent().getExtras().getString("keyword");
        Log.d(TAG, "onCreate: "+KEYWORD);
        clickSearch(KEYWORD);
    }

    private void clickSearch(String keyword) {
        RetrofitClient.getRetrofitInterface().get_법률안심사및처리_의안검색(KEY,"JSON","1","10",keyword)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Log.d(TAG, "onResponse: "+response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            JSONArray jsonArray=new JSONArray(jsonObject.getString("TVBPMBILL11"));
                            JSONObject jsonObject1=new JSONObject(jsonArray.getString(0));
                            JSONArray headArray=new JSONArray(jsonObject1.getString("head"));
                            JSONObject list_total_count=new JSONObject(headArray.getString(0));
                            Log.d(TAG, "list_total_count: "+list_total_count.getString("list_total_count"));
                            int cnt= (int) Math.floor(Integer.parseInt(list_total_count.getString("list_total_count"))/10);
                            Log.d(TAG, "cnt: "+cnt);
                            RetrofitClient.getRetrofitInterface().get_법률안심사및처리_의안검색(KEY,"JSON",cnt+"","10",keyword)
                                    .enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            JSONObject jsonObject= null;
                                            try {
                                                jsonObject = new JSONObject(response.body());
                                                JSONArray jsonArray=new JSONArray(jsonObject.getString("TVBPMBILL11"));
                                                JSONObject jsonObject1=new JSONObject(jsonArray.getString(1));
                                                JSONArray row=new JSONArray(jsonObject1.getString("row"));
                                                Log.d(TAG, "row: "+row);
                                                for(int i=0;i<row.length();i++){
                                                    JSONObject element= (JSONObject) row.get(i);
                                                    Log.d(TAG, "element: "+element);
                                                    RowData rowData=new RowData(
                                                            element.getString("BILL_NO"),
                                                            element.getString("BILL_NAME"),
                                                            element.getString("PROPOSER"),
                                                            element.getString("PROPOSE_DT"),
                                                            element.getString("CURR_COMMITTEE_ID"),
                                                            element.getString("CURR_COMMITTEE"),
                                                            element.getString("LINK_URL"),
                                                            1
                                                    );
                                                    rowList.add(rowData);
                                                }
                                                setRecyclerview(rowList);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d(TAG, "onFailure: "+t.getMessage());
                                        }
                                    });
                            //Log.d(TAG, "jsonArray[1]: "+jsonArray.get(1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    private void setRecyclerview(ArrayList<RowData> list) {
        rowAdapter=new RowAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));

        rowAdapter.setOnItemClickListener((v, pos) -> {
            if("코로나".equals(KEYWORD)){
                list.get(pos).setStep(3);
            }
            startActivity(new Intent(this,RowDetailActivity.class)
                    .putExtra("row",list.get(pos))
                    .putExtra("keyword",KEYWORD)
            );
        });
        recyclerView.setAdapter(rowAdapter);
    }

}

