package com.example.team_project.MainFragment;

import static com.example.team_project.Retrofit.RetrofitClient.KEY;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.Adapter.FavAdapter;
import com.example.team_project.Adapter.FavData;
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
    private int i=0;
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
        // 본회의 심의 -> 정부이송

        return binding.getRoot();
    }



    private void showNotification(int step, RowData rowData) {
        createNotificationChannel();
        String title="";
        String status="";
        int icon_image=0;
        switch (step){
            case 2:
                status="[입법예고 진행 중]";
                icon_image=R.drawable.icon_step2;
                break;
            case 3:
                status="[위원회/체계자구심사 진행 중]";
                icon_image=R.drawable.icon_step3;
                break;
            case 4:
                status="[현재 본회의 심의로 이동]";
                icon_image=R.drawable.icon_step4;
                break;
            case 5:
                Log.d(TAG, "showNotification: 5");
                status="[법률안 정부이송]";
                icon_image=R.drawable.icon_step5;
                break;
        }
        title="<b>"+rowData.getBill_name()+"</b>";
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getActivity(),RowDetailActivity.class)
                .putExtra("row",rowData)
                , PendingIntent.FLAG_UPDATE_CURRENT); //PendingIntent.FLAG_MUTABLE or FLAG_IMMUTABLE
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), i+"")
                .setSmallIcon(icon_image) //설정한 작은 아이콘. 사용자가 볼 수 있는 유일한 필수 콘텐츠입니다.
                .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), icon_image))
                //.setContentTitle(title) // 설정한 제목
                .setContentTitle(Html.fromHtml(title)) // 설정한 제목
                .setContentText(status) //본문 텍스트
                .setAutoCancel(true) //사용자가 알림을 탭하면 자동으로 알림을 삭제합니다.
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getContext().getResources(), icon_image)))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH); //알림 우선순위.Android 8.0 이상의 경우 다음 섹션에 표시된 채널 중요도를 대신 설정해야 합니다

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(0,builder.build());
        i++;
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because // 알림 채널을 생성하지만 API 26+에서만 다음을 수행할 수 있습니다.
        // the NotificationChannel class is new and not in the support library // Notification Channel 클래스가 새 클래스로 지원 라이브러리에 없음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="CHANNEL"; //채널명
            String description = "description"; //채널 설명
            int importance = NotificationManager.IMPORTANCE_HIGH; //중요도
            NotificationChannel channel = new NotificationChannel(i+"", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance // 채널을 시스템에 등록합니다. 중요도는 변경할 수 없습니다.
            // or other notification behaviors after this // 또는 이 이후의 다른 알림 동작
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void getFavData() {
        favList=SharedPreference.getInstance(getContext()).getFavList();
        if(favList.size()==0){
            binding.text.setVisibility(View.VISIBLE);
            recyclerView_fav.setVisibility(View.GONE);
        }else {
            binding.text.setVisibility(View.GONE);
            recyclerView_fav.setVisibility(View.VISIBLE);
        }
        favAdapter=new FavAdapter(favList);
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false));

        favAdapter.setOnItemClickListener((v, pos) -> {
            startActivity(new Intent(getActivity(),RowDetailActivity.class).putExtra("row",favList.get(pos)));
            Log.d(TAG, "상세보기 이동 onResponse: row = " + rowList.get(pos) );
        });

        recyclerView_fav.setAdapter(favAdapter);
        //setRecyclerViewEvent(recyclerView_fav);
        //showNotification(2,favList.get(0));
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

                           /* // index = 0
                            rowList.add(new RowData("2118059", "양식산업발전법 일부개정법률안", "김승남의원 등 10인", "2022-11-02",
                                    null,null, "http://likms.assembly.go.kr/bill/billDetail.do?billId=PRC_Q2U2A0V6X1V3Q0D9K2W6W2N3R7J1V0&ageFrom=21&ageTo=21", 0));
                            // index = 1
                            rowList.add(new RowData("2118058", "의료법 일부개정법률안", "인재근의원 등 11인", "2022-11-02",
                                    null,null, "http:\\/\\/likms.assembly.go.kr\\/bill\\/billDetail.do?billId=PRC_N2O2P1E1L0G2I1G7N1C5E0C8A4U0S3&ageFrom=21&ageTo=21", 0));
                            // index = 2
                            rowList.add(new RowData("2118057", "조세특례제한법 일부개정법률안", "이용의원등15인", "2022-11-02",
                                    null,null, "http:\\/\\/likms.assembly.go.kr\\/bill\\/billDetail.do?billId=PRC_E2X2B1P0M2E8J1B4G0Y5N3G2W0A6H6&ageFrom=21&ageTo=21", 0));*/
                            for(int i=0;i<3;i++){

                                // 일일히 데이터 추가하기
                                // row list 를 만들어야 한다.

                                if (!"null".equals(row.getJSONObject(i).getString("COMMITTEE"))){
                                    rowList.add(new RowData(
                                                    row.getJSONObject(i).getString("BILL_NO"),
                                                    row.getJSONObject(i).getString("BILL_NAME"),
                                                    row.getJSONObject(i).getString("PROPOSER"),
                                                    row.getJSONObject(i).getString("PROPOSE_DT"),
                                                    row.getJSONObject(i).getString("COMMITTEE_ID"),
                                                    row.getJSONObject(i).getString("COMMITTEE"),
                                                    row.getJSONObject(i).getString("DETAIL_LINK"),
                                                    0
                                            ));
                                }else {
                                    rowList.add(new RowData(
                                            row.getJSONObject(i).getString("BILL_NO"),
                                            row.getJSONObject(i).getString("BILL_NAME"),
                                            row.getJSONObject(i).getString("PROPOSER"),
                                            row.getJSONObject(i).getString("PROPOSE_DT"),
                                            "-",
                                            "-",
                                            row.getJSONObject(i).getString("DETAIL_LINK"),
                                            0
                                    ));
                                }

                            }

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