package com.example.team_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.team_project.Adapter.RowAdapter;
import com.example.team_project.Adapter.RowData;
import com.example.team_project.databinding.ActivityLatestRowListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// 최신 법률안 리스트 모아보기 액티비티
public class LatestRowListActivity extends AppCompatActivity {
    private final String TAG=this.getClass().getSimpleName();
    ActivityLatestRowListBinding binding;
    private RowAdapter rowAdapter;
    private RecyclerView recyclerView;
    private ArrayList<RowData> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLatestRowListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView=binding.rv;
        String rowStr=getIntent().getStringExtra("row");
        try {
            JSONArray jsonArray=new JSONArray(rowStr);
            Log.d(TAG, "onCreate: "+jsonArray.length());
/*            // index = 0, 1, 2 만 고정 시키기
            // index = 0
            list.add(new RowData("2118059", "양식산업발전법 일부개정법률안", "김승남의원 등 10인", "2022-11-02",
                    null,null, "http://likms.assembly.go.kr/bill/billDetail.do?billId=PRC_Q2U2A0V6X1V3Q0D9K2W6W2N3R7J1V0&ageFrom=21&ageTo=21", 0));
            // index = 1
            list.add(new RowData("2118058", "의료법 일부개정법률안", "인재근의원 등 11인", "2022-11-02",
                    null,null, "http:\\/\\/likms.assembly.go.kr\\/bill\\/billDetail.do?billId=PRC_N2O2P1E1L0G2I1G7N1C5E0C8A4U0S3&ageFrom=21&ageTo=21", 0));
            // index = 2
            list.add(new RowData("2118057", "조세특례제한법 일부개정법률안", "이용의원등15인", "2022-11-02",
                    null,null, "http:\\/\\/likms.assembly.go.kr\\/bill\\/billDetail.do?billId=PRC_E2X2B1P0M2E8J1B4G0Y5N3G2W0A6H6&ageFrom=21&ageTo=21", 0));
//

            // index 3번 부터 for문 돌아감.
            for(int i=3;i<jsonArray.length();i++){
                JSONObject element=jsonArray.getJSONObject(i);
                if(null==(element.getString("COMMITTEE"))){
                    RowData rowData=new RowData(
                            element.getString("BILL_NO"),
                            element.getString("BILL_NAME"),
                            element.getString("PROPOSER"),
                            element.getString("PROPOSE_DT"),
                            element.getString("CURR_COMMITTEE_ID"),
                            element.getString("CURR_COMMITTEE"),
                            element.getString("LINK_URL"),
                            0
                    );
                    list.add(rowData);
                }else {
                    RowData rowData=new RowData(
                            element.getString("BILL_NO"),
                            element.getString("BILL_NAME"),
                            element.getString("PROPOSER"),
                            element.getString("PROPOSE_DT"),
                            "-",
                            ("-"),
                            element.getString("DETAIL_LINK"),
                            0
                    );
                    list.add(rowData);
                }
            }*/

//

            // index 3번 부터 for문 돌아감.
            for(int i=0;i<jsonArray.length();i++){
                JSONObject element=jsonArray.getJSONObject(i);
                if(null==(element.getString("COMMITTEE"))){
                    RowData rowData=new RowData(
                            element.getString("BILL_NO"),
                            element.getString("BILL_NAME"),
                            element.getString("PROPOSER"),
                            element.getString("PROPOSE_DT"),
                            element.getString("CURR_COMMITTEE_ID"),
                            element.getString("CURR_COMMITTEE"),
                            element.getString("LINK_URL"),
                            0
                    );
                    list.add(rowData);
                }else {
                    RowData rowData=new RowData(
                            element.getString("BILL_NO"),
                            element.getString("BILL_NAME"),
                            element.getString("PROPOSER"),
                            element.getString("PROPOSE_DT"),
                            "-",
                            ("-"),
                            element.getString("DETAIL_LINK"),
                            0
                    );
                    list.add(rowData);
                }
            }
            setRecyclerview(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setRecyclerview(ArrayList<RowData> list) {
        rowAdapter=new RowAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        rowAdapter.setOnItemClickListener((v, pos) -> {
            startActivity(new Intent(this,RowDetailActivity.class)
                            .putExtra("row",list.get(pos))
                    // putextra 해서 최신에서 넘어가는 건지 아닌지 구분하기
                    // key = step (1~5)
            );

        });
        recyclerView.setAdapter(rowAdapter);
    }

}
