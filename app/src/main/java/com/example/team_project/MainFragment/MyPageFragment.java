package com.example.team_project.MainFragment;

import static com.example.team_project.Retrofit.RetrofitNaverSearch.client_id;
import static com.example.team_project.Retrofit.RetrofitNaverSearch.client_pw;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.team_project.Adapter.NewsData;
import com.example.team_project.Retrofit.ApiInterface;
import com.example.team_project.Retrofit.RetrofitNaverSearch;
import com.example.team_project.databinding.FragmentMyPageBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends Fragment {
    private final String TAG=this.getClass().getSimpleName();
    private FragmentMyPageBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentMyPageBinding.inflate(inflater,container,false);

//        binding.step1.setOnClickListener(view -> {
//            searchNaverNews("발의");
//        });
//        binding.step2.setOnClickListener(view -> {
//            searchNaverNews("입법예고");
//        });
//        binding.step3.setOnClickListener(view -> {
//            searchNaverNews("법사위심사");
//        });
//        binding.step4.setOnClickListener(view -> {
//            searchNaverNews("본회의심의");
//        });
//        binding.step5.setOnClickListener(view -> {
//            searchNaverNews("법률안 이송");
//        });
        return binding.getRoot();
    }

}