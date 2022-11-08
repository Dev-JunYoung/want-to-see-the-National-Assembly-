package com.example.team_project.MainFragment;

import static com.example.team_project.Retrofit.RetrofitClient.KEY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.Adapter.FavAdapter;
import com.example.team_project.Adapter.LatestAdapter;
import com.example.team_project.Adapter.RowData;
import com.example.team_project.Event.LinePagerIndicatorDecoration;
import com.example.team_project.Event.SnapPagerScrollListener;
import com.example.team_project.LatestRowListActivity;
import com.example.team_project.R;
import com.example.team_project.Retrofit.RetrofitClient;
import com.example.team_project.RowDetailActivity;
import com.example.team_project.RowProcessActivity;
import com.example.team_project.API.SharedPreference;
import com.example.team_project.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG=this.getClass().getSimpleName();
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView_latest;
    private RecyclerView recyclerView_fav;
    private LatestAdapter latestAdapter;
    private FavAdapter favAdapter;
    private ArrayList<RowData> rowList=new ArrayList<>();
    private ArrayList<RowData> favList=new ArrayList<>();
    private JSONArray row;
    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        recyclerView_latest=binding.rvLatest;
        recyclerView_fav=binding.rvFav;
        getData();

        binding.btnDetail.setOnClickListener(this);

        binding.step1.setOnClickListener(view -> {
            searchNaverNews("발의");
        });
        binding.step2.setOnClickListener(view -> {
            searchNaverNews("입법예고");
        });
        binding.step3.setOnClickListener(view -> {
            searchNaverNews("위원회 심사");
        });
        binding.step4.setOnClickListener(view -> {
            searchNaverNews("본회의 심의");
        });
        binding.step5.setOnClickListener(view -> {
            searchNaverNews("이송");
        });

        return binding.getRoot();
    }

    private void getFavData() {
        favList=SharedPreference.getInstance(getContext()).getFavList();
        favAdapter=new FavAdapter(favList);
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false));

        favAdapter.setOnItemClickListener((v, pos) -> {
            startActivity(new Intent(getActivity(),RowDetailActivity.class).putExtra("row",favList.get(pos)));
            Log.d(TAG, "상세보기 이동 onResponse: row = " + rowList.get(pos) );
        });

        recyclerView_fav.setAdapter(favAdapter);
        //setRecyclerViewEvent(recyclerView_fav);
    }


    private void getData() {
        RetrofitClient.getRetrofitInterface().get_발의법률안(KEY,"JSON","1","130","21")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onResponse: "+response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            JSONArray jsonArray=new JSONArray(jsonObject.getString("nzmimeepazxkubdpn"));

                            JSONArray head=new JSONArray(jsonObject.getString("nzmimeepazxkubdpn")).getJSONObject(0).getJSONArray("head");
                            row=new JSONArray(jsonObject.getString("nzmimeepazxkubdpn")).getJSONObject(1).getJSONArray("row");
                            Log.d(TAG, "row: "+row);

                            // index = 0
                            rowList.add(new RowData("2118059", "양식산업발전법 일부개정법률안", "김승남의원 등 10인", "2022-11-02",
                                    null,null, "http://likms.assembly.go.kr/bill/billDetail.do?billId=PRC_Q2U2A0V6X1V3Q0D9K2W6W2N3R7J1V0&ageFrom=21&ageTo=21", 0));
                            // index = 1
                            rowList.add(new RowData("2118058", "의료법 일부개정법률안", "인재근의원 등 11인", "2022-11-02",
                                    null,null, "http:\\/\\/likms.assembly.go.kr\\/bill\\/billDetail.do?billId=PRC_N2O2P1E1L0G2I1G7N1C5E0C8A4U0S3&ageFrom=21&ageTo=21", 0));
                            // index = 2
                            rowList.add(new RowData("2118057", "조세특례제한법 일부개정법률안", "이용의원등15인", "2022-11-02",
                                    null,null, "http:\\/\\/likms.assembly.go.kr\\/bill\\/billDetail.do?billId=PRC_E2X2B1P0M2E8J1B4G0Y5N3G2W0A6H6&ageFrom=21&ageTo=21", 0));
//                            for(int i=0;i<3;i++){
//
//                                // 일일히 데이터 추가하기
//                                // row list 를 만들어야 한다.
//
//
//                                if (!"null".equals(row.getJSONObject(i).getString("COMMITTEE"))){
//                                    rowList.add(new RowData(
//                                                    row.getJSONObject(i).getString("BILL_NO"),
//                                                    row.getJSONObject(i).getString("BILL_NAME"),
//                                                    row.getJSONObject(i).getString("PROPOSER"),
//                                                    row.getJSONObject(i).getString("PROPOSE_DT"),
//                                                    row.getJSONObject(i).getString("COMMITTEE_ID"),
//                                                    row.getJSONObject(i).getString("COMMITTEE"),
//                                                    row.getJSONObject(i).getString("DETAIL_LINK")
//                                            ));
//                                }else {
//                                    rowList.add(new RowData(
//                                            row.getJSONObject(i).getString("BILL_NO"),
//                                            row.getJSONObject(i).getString("BILL_NAME"),
//                                            row.getJSONObject(i).getString("PROPOSER"),
//                                            row.getJSONObject(i).getString("PROPOSE_DT"),
//                                            "-",
//                                            "-",
//                                            row.getJSONObject(i).getString("DETAIL_LINK")
//                                    ));
//                                }
//
//                            }

                            latestAdapter=new LatestAdapter(rowList);
                            recyclerView_latest.setLayoutManager(new LinearLayoutManager(
                                    getActivity(), LinearLayoutManager.HORIZONTAL, false));

                            // 최신 법률안 아이템 클릭시 법률안 상세보기로 이동
                            latestAdapter.setOnItemClickListener((v, pos) -> {
                                startActivity(new Intent(getActivity(),RowDetailActivity.class).putExtra("row",rowList.get(pos)));
                                Log.d(TAG, "상세보기 이동 onResponse: row = " + rowList.get(pos) );
                            });

                            recyclerView_latest.setAdapter(latestAdapter);
                            setRecyclerViewEvent(recyclerView_latest);



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
    PagerSnapHelper snapHelper;
    private void setRecyclerViewEvent(RecyclerView recyclerView) {
        if(snapHelper==null){
            Log.d(TAG, "setRecyclerViewEvent: "+snapHelper);
            snapHelper = new PagerSnapHelper();
            recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
            snapHelper.attachToRecyclerView(recyclerView);
            SnapPagerScrollListener listener = new SnapPagerScrollListener(
                    new PagerSnapHelper(),
                    SnapPagerScrollListener.ON_SCROLL,
                    true,
                    new SnapPagerScrollListener.OnChangeListener() {
                        @Override
                        public void onSnapped(int position) {
                            //position 받아서 이벤트 처리
                            Log.d(TAG, "onSnapped: "+position);
                        }
                    }
            );
            recyclerView.addOnScrollListener(listener);
        }
    }

    // 입법 과정 선택했을 때 뉴스 보여주는 메서드 || 입법과정 액티비티 이동
    private void searchNaverNews(String keyword) {
        Log.d(TAG, "searchNaverNews: "+keyword);
        startActivity(new Intent(getActivity(), RowProcessActivity.class).putExtra("keyword",keyword));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_detail:
                startActivity(new Intent(getActivity(), LatestRowListActivity.class).putExtra("row", String.valueOf(row)));
                break;
        }
    }
    @Override
    public void onResume() { Log.d(TAG, "onResume: ");
        super.onResume();
        getFavData();
    }

    @Override
    public void onStart() { Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onDestroyView() { Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }


    @Override
    public void onDestroy() { Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}