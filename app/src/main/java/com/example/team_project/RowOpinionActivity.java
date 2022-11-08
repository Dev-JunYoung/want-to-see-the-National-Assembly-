package com.example.team_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team_project.API.SharedPreference;
import com.example.team_project.Adapter.OpinionAdapter;
import com.example.team_project.Adapter.OpinionData;
import com.example.team_project.Adapter.RowData;
import com.example.team_project.databinding.ActivityRowOpinionBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class RowOpinionActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=this.getClass().getSimpleName();
    private ActivityRowOpinionBinding binding;
    private RowData data;

    private RecyclerView recyclerView;
    private OpinionAdapter agreeAdapter;

    JSONArray agreeJsonArray;
    JSONArray disagreeJsonArray;


    private ArrayList<OpinionData> agreeList=new ArrayList<>();
    private ArrayList<OpinionData> disagreeList=new ArrayList<>();

    private ArrayList<OpinionData> commentDataList=new ArrayList<>();


    final String 반대_COLOR="#FB7575";
    final String 찬성_COLOR="#7A90FF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        binding=ActivityRowOpinionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        data= (RowData) getIntent().getSerializableExtra("data");
        recyclerView=binding.rv;
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, true));

        getSharedData();


        Log.d(TAG, "agreeJsonArray: "+agreeJsonArray);
        Log.d(TAG, "disagreeJsonArray: "+disagreeJsonArray);

        Log.d(TAG, "agreeList: "+agreeList);
        Log.d(TAG, "disagreeList: "+disagreeList);

        setAgreeRecyclerview();

        if(19<data.getBill_name().length()){
            binding.tvRowName.setText(data.getBill_name().substring(0,19)+"...");
        }else {
            binding.tvRowName.setText(data.getBill_name());
        }
        binding.tvVoting.setOnClickListener(this);
        binding.btnCommittee.setOnClickListener(this);
        binding.btnCommittee2.setOnClickListener(this);
        binding.btnCommittee2Right.setOnClickListener(this);
        binding.btnCommitteeLeft.setOnClickListener(this);

    }

    private void getSharedData() {
        Gson gson=new Gson();
        SharedPreferences sharedPreferences=getSharedPreferences("voting",0);
        String jsonCurrent=sharedPreferences.getString(data.getBill_no(),null);
        if(jsonCurrent==null){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonCurrent);
            JSONObject jsonObject1 = jsonObject.getJSONObject("nameValuePairs");
            agreeJsonArray = jsonObject1.getJSONArray("agree");
            disagreeJsonArray = jsonObject1.getJSONArray("disagree");

            for(int i=0;i<agreeJsonArray.length();i++){
                Type typeCurrent=new TypeToken<OpinionData>(){}.getType();
                OpinionData opinionData=gson.fromJson(String.valueOf(agreeJsonArray.getJSONObject(i)),typeCurrent);
                if(opinionData.getContent().equals("")){
                    Log.d(TAG, "getSharedData: null");
                }else {
                    agreeList.add(opinionData);
                }
            }
            for(int i=0;i<disagreeJsonArray.length();i++){
                Type typeCurrent=new TypeToken<OpinionData>(){}.getType();
                OpinionData opinionData=gson.fromJson(String.valueOf(disagreeJsonArray.getJSONObject(i)),typeCurrent);
                if(opinionData.getContent().equals("")){
                    Log.d(TAG, "getSharedData: null");
                }else {
                    disagreeList.add(opinionData);
                }
            }

            int 전체=agreeJsonArray.length()+disagreeJsonArray.length();
            binding.tvTotalCnt.setText("전체 : "+전체);
            binding.tvAgree.setText("찬성 : "+agreeJsonArray.length());
            binding.tvDisagree.setText("반대 : "+disagreeJsonArray.length());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setDisAgreeRecyclerview() {
        Log.d(TAG, "setDisAgreeRecyclerview: ");
        commentDataList.clear();
        for(int i=0;i<disagreeList.size();i++){
            if("null".equals(disagreeList.get(i).getContent())){

            }else {
                commentDataList.add(disagreeList.get(i));
            }

        }
        agreeAdapter=new OpinionAdapter(commentDataList);
        recyclerView.setAdapter(agreeAdapter);
        setChart("반대");
    }
    private void setAgreeRecyclerview() {
        Log.d(TAG, "setAgreeRecyclerview: ");
        agreeAdapter=new OpinionAdapter(commentDataList);
        commentDataList.clear();
        for(int i=0;i<agreeList.size();i++){
            if("null".equals(agreeList.get(i).getContent())){
            }else {
                commentDataList.add(agreeList.get(i));
            }
        }
        recyclerView.setAdapter(agreeAdapter);
        setChart("찬성");
    }
    private void setChart(String 의견) {
        if(agreeJsonArray==null &&disagreeJsonArray==null ) return;
        binding.chart.clearChart();
        if("찬성".equals(의견)){
            if(agreeJsonArray.length()==0){
                Log.d(TAG, "agreeList: size == 0");
                binding.chart.setInnerValueUnit("(반대 수)");
            }else {
                Log.d(TAG, "agreeList: size != 0");
                binding.chart.setInnerValueUnit("(찬성 수)");
            }
            binding.chart.addPieSlice(new PieModel("찬성", agreeJsonArray.length(), Color.parseColor(찬성_COLOR)));
            binding.chart.addPieSlice(new PieModel("반대",disagreeJsonArray.length(), Color.parseColor(반대_COLOR)));
            binding.chart.startAnimation();
        }else {
            if(disagreeJsonArray.length()==0){
                Log.d(TAG, "agreeList: size == 0");
                binding.chart.setInnerValueUnit("(찬성 수)");
            }else {
                Log.d(TAG, "agreeList: size != 0");
                binding.chart.setInnerValueUnit("(반대 수)");
            }
            binding.chart.addPieSlice(new PieModel("반대", disagreeJsonArray.length(), Color.parseColor(반대_COLOR)));
            binding.chart.addPieSlice(new PieModel("찬성",agreeJsonArray.length(), Color.parseColor(찬성_COLOR)));
            binding.chart.startAnimation();
            binding.chart.setInnerValueUnit("(반대 수)");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_committee:
                binding.tabLeft.setVisibility(View.VISIBLE);
                binding.tabRight.setVisibility(View.GONE);
                Log.d(TAG, "찬성: ");
                setAgreeRecyclerview();
                break;
            case R.id.btn_committee_left:
                binding.tabLeft.setVisibility(View.VISIBLE);
                binding.tabRight.setVisibility(View.GONE);
                Log.d(TAG, "찬성");
                setAgreeRecyclerview();
                break;
            case R.id.btn_committee2_right:
                Log.d(TAG, "반대: ");
                setDisAgreeRecyclerview();
                break;
            case R.id.btn_committee2:
                binding.tabLeft.setVisibility(View.GONE);
                binding.tabRight.setVisibility(View.VISIBLE);
                setDisAgreeRecyclerview();
                Log.d(TAG, "반대: ");
                break;
            case R.id.tv_voting:
                voting();
                break;
        }
        //binding.btnCommittee.setBackgroundColor(R.color.back);
    }

    private void voting() {
        Dialog dilaog01 = new Dialog(this);       // Dialog 초기화
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dilaog01.setContentView(R.layout.dialog_voting);

        dilaog01.show(); // 다이얼로그 띄우기

        Button btn_agree = dilaog01.findViewById(R.id.btn_agree);
        Button btn_disagree = dilaog01.findViewById(R.id.btn_disagree);
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //agreeList.add(new OpinionData("찬성합니다","김OO",data.getPropose_dt()));
                setChart("찬성");
                dilaog01.dismiss();
                의견남기기("찬성");
            }
        });
        btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disagreeList.add(new OpinionData("반대합니다","김OO",data.getPropose_dt()));
                setChart("반대");
                dilaog01.dismiss();
                의견남기기("반대");
            }
        });
    }
    void 의견남기기(String 의견){
        Dialog dilaog01 = new Dialog(this);       // Dialog 초기화
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dilaog01.setContentView(R.layout.custom_dialog);
        dilaog01.show(); // 다이얼로그 띄우기

        EditText et_comment=dilaog01.findViewById(R.id.et_comment);
        EditText et_reg_name=dilaog01.findViewById(R.id.et_reg_name);
        Button btn_ok = dilaog01.findViewById(R.id.btn_ok);
        Button btn_cancel = dilaog01.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RowOpinionActivity.this, "ok", Toast.LENGTH_SHORT).show();
                String comment=et_comment.getText().toString();
                String reg_name=et_reg_name.getText().toString();
                if(의견.equals("찬성")) {
                    agreeList.add(new OpinionData(comment,reg_name,getToday("yyyy-MM-dd")));
                }else {
                    disagreeList.add(new OpinionData(comment,reg_name,getToday("yyyy-MM-dd")));
                }
                SharedPreferences sharedPreferences=getSharedPreferences("voting",0);
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
                editor.putString(data.getBill_no(),json);
                editor.apply();
                String no =data.getBill_no();
                Log.d(TAG, "onClick: "+sharedPreferences.getString(no,null));
                dilaog01.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RowOpinionActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                dilaog01.dismiss();
            }
        });
    }
    public static String getToday(String pattern){ //yyyyMMdd,yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());
        return strToday;
    }

}