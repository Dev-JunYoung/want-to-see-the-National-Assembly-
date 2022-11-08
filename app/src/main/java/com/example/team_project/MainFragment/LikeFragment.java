package com.example.team_project.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.API.SharedPreference;
import com.example.team_project.Adapter.FavAdapter;
import com.example.team_project.Adapter.RowData;
import com.example.team_project.RowDetailActivity;
import com.example.team_project.databinding.FragmentLikeBinding;

import java.util.ArrayList;

public class LikeFragment extends Fragment {
    private final String TAG=this.getClass().getSimpleName();
    private FragmentLikeBinding binding;
    private ArrayList<RowData> favList=new ArrayList<>();

    private FavAdapter favAdapter;
    private RecyclerView recyclerView_fav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                binding= FragmentLikeBinding.inflate(inflater,container,false);
        recyclerView_fav=binding.rv;
        getFavData();
        return binding.getRoot();
    }

    private void getFavData() {
        favList=SharedPreference.getInstance(getContext()).getFavList();
        favAdapter=new FavAdapter(favList);
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        favAdapter.setOnItemClickListener((v, pos) -> {
            startActivity(new Intent(getActivity(), RowDetailActivity.class).putExtra("row",favList.get(pos)));
        });

        recyclerView_fav.setAdapter(favAdapter);
    }

}