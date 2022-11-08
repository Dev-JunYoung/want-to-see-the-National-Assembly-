package com.example.team_project;


import static android.graphics.Color.WHITE;

import static com.example.team_project.Retrofit.RetrofitNaverSearch.client_id;
import static com.example.team_project.Retrofit.RetrofitNaverSearch.client_pw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.example.team_project.Adapter.NewsAdapter;
import com.example.team_project.Adapter.NewsData;
import com.example.team_project.Retrofit.ApiInterface;
import com.example.team_project.Retrofit.RetrofitNaverSearch;
import com.example.team_project.databinding.ActivityRowProcessBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RowProcessActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=this.getClass().getSimpleName();
    private ActivityRowProcessBinding binding;
    private String keyword;
    private ArrayList<NewsData> newsList=new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRowProcessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView=binding.rv;

        keyword=getIntent().getStringExtra("keyword");
        setUI(keyword);



        binding.btnCommittee.setOnClickListener(this);
        binding.btnCommittee2.setOnClickListener(this);
        binding.btnCommittee2Right.setOnClickListener(this);
        binding.btnCommitteeLeft.setOnClickListener(this);

    }

    private void setUI(String keyword) {
        Log.d(TAG, "setUI: "+keyword);
        binding.toolbar.setTitle(keyword);
        binding.tvRowName.setText(keyword+"란?");
        switch (keyword){
            case "발의": get발의();
                break;
            case "입법예고": get입법예고();
                break;
            case "본회의 심의":get본회의심의();
                break;
            case "이송":get이송();
                binding.text.setText("'"+keyword+"' "+"과 관련된 최신 뉴스");
                getNews("법률안"+keyword);
                return;
            case "위원회 심사":get위원회심사();
                break;
            case  "체계자구심사":get체계자구심사();
                break;
        }
        binding.text.setText("'"+keyword+"' "+"와 관련된 최신 뉴스");
        getNews(keyword);

    }

    private void getNews(String keyword) {
        keyword="국회 "+keyword;
        RetrofitNaverSearch.getInstance().create(ApiInterface.class).getSearchResult(
                client_id,client_pw,"news.json",keyword)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null){
                            String result = response.body();
                            try {
                                JSONObject jsonObject=new JSONObject(result);
                                JSONArray jsonArray=new JSONArray(jsonObject.getString("items"));
                                for(int i=0;i<jsonArray.length();i++){
                                    NewsData newsData=new NewsData(
                                            jsonArray.getJSONObject(i).getString("title"),
                                            jsonArray.getJSONObject(i).getString("originallink"),
                                            jsonArray.getJSONObject(i).getString("link"),
                                            jsonArray.getJSONObject(i).getString("description"),
                                            jsonArray.getJSONObject(i).getString("pubDate")
                                    );
                                    newsList.add(newsData);
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(
                                        getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                newsAdapter=new NewsAdapter(newsList);
                                newsAdapter.setOnItemClickListener((v, pos) -> {
                                    startActivity(new Intent(getApplicationContext(),WebViewActivity.class)
                                            .putExtra("url",newsList.get(pos).getLink()));
                                });

                                recyclerView.setAdapter(newsAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "성공 : " + result);
                        }else{
                            Log.e(TAG, "실패 : " + response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    private final String 발의="발의란 국회에서 국회의원이 의안을 제출하는 일. ";
    private final String 발의내용="발의안은 의원 10인 이상의 찬성으로 발의할 수 있으며 의안을 발의하는 의원은 그 안을 갖추고 이유를 붙여 소정의 찬성자와 연서(連書)하여 이를 의장에게 제출해야 한다.";
    void get발의(){
        binding.tvRowDefine.setText(발의);
        binding.tvRowContent.setText(발의내용);
    }
    private final String 입법예고="국민의 일상생활이나 권리와 직결되는 법령 따위를 만들거나 수정할 때, 입법안의 내용을 미리 알려 국민의 의사를 반영하기 위해 마련한 제도. 특별한 사정이 없는 한 20일간 공지한다.";
    void get입법예고(){
        binding.tvRowDefine.setText(입법예고);
    }
    private final String 위원회심사="소관 상임위원회에서는 전체회의 또는 소위원회를 구성하여 법률안을 심사하며, 필요한 경우에는 공청회를 개최하여 이해관계인의 의견을 듣고 심사를 하기도 한다.";
    private final String 위원회심사내용="소관 상임위원회 전체회의의 의결을 거친 법률안은 다시 법률안의 자구와 체계 심사를 위하여 법제사법위원회에 회부된다.";
    @SuppressLint("ResourceAsColor")
    void get위원회심사() {
        binding.tabLeft.setVisibility(View.VISIBLE);
        binding.tabRight.setVisibility(View.GONE);
        binding.tvRowDefine.setText(위원회심사);
    }
    private final String 이송="국회에서 의결된 의안은 국회 의장이 정부에 이송한다.";
    private final String 이송내용="정부는 대통령이 법률안을 공포한 경우에는 이를 지체 없이 국회에 통지하여야 한다.\n" +
            "\n" +
            "대통령이 확정된 법률을 공포하지 아니하였을 때에는 의장은 그 공포기일이 경과한 날부터 5일 이내에 공포하고, 대통령에게 통지하여야 한다.";
    void get이송(){
        binding.tvRowDefine.setText(이송);
        binding.tvRowContent.setText(이송내용);
    }
    private final String 체계자구심사="체계 심사는 법률과 다른 것과 상충되는 문제, 법률안 안에서의 균형이나 저촉이 되는지 위헌을 다루는 것이고 자구는 용어의 적합성에 대한 문제를 다루는 것을 의미한다.";
    void get체계자구심사(){
        binding.tabLeft.setVisibility(View.GONE);
        binding.tabRight.setVisibility(View.VISIBLE);
        binding.tvRowDefine.setText(체계자구심사);
    }


    private final String 본회의심의="국회의 의사와 각 상임 위원회에서 심사한 안건을 최종적으로 결정하는 회의.";
    private final String 본회의심의내용=" 의안에 대한 심의와 함께 대통령의 예산안 시정 연설, 각 교섭 단체의 대표 연설 및 대정부 질문 등 국정 전반에 대한 토론을 한다.";
    void get본회의심의(){
        binding.tvRowDefine.setText(본회의심의);
        binding.tvRowContent.setText(본회의심의내용);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_committee:
                binding.tabLeft.setVisibility(View.VISIBLE);
                binding.tabRight.setVisibility(View.GONE);
                Log.d(TAG, "btn_committee: ");
                setUI("위원회심사");
                break;
            case R.id.btn_committee_left:
                binding.tabLeft.setVisibility(View.VISIBLE);
                binding.tabRight.setVisibility(View.GONE);
                Log.d(TAG, "btn_committee_left");
                setUI("위원회심사");
                break;
            case R.id.btn_committee2_right:
                Log.d(TAG, "btn_committee2_right: ");
                setUI("체계자구심사");
                break;
            case R.id.btn_committee2:
                binding.tabLeft.setVisibility(View.GONE);
                binding.tabRight.setVisibility(View.VISIBLE);
                Log.d(TAG, "btn_committee2: ");
                setUI("체계자구심사");
                break;
        }
        //binding.btnCommittee.setBackgroundColor(R.color.back);
    }
}