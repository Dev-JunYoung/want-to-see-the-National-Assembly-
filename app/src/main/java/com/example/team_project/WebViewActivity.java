package com.example.team_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.team_project.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {
    private final String TAG=this.getClass().getSimpleName();
    private ActivityWebViewBinding binding;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        binding=ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        url=getIntent().getStringExtra("url");
        binding.webView.loadUrl(url);
        //binding.webView.loadUrl("http://likms.assembly.go.kr/bill/billDetail.do?billId=PRC_T2S2B1Z0I2N8V1O6U1Q0O2F5P3U5L3&ageFrom=21&ageTo=21");
        binding.webView.getSettings().setUseWideViewPort(true); // wide viewport를 사용하도록 설정
        binding.webView.getSettings().setLoadWithOverviewMode(true); // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        binding.webView.getSettings().setBuiltInZoomControls(true); //zoom 허용
        binding.webView.getSettings().setSupportZoom(true); //zoom 허용
    }
}