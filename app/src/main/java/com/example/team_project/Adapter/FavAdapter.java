package com.example.team_project.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.databinding.ItemFavBinding;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder>{
    private ItemFavBinding binding;
    private ArrayList<RowData> mList;
    private final String TAG=this.getClass().getSimpleName();

    public FavAdapter(ArrayList<RowData> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ItemFavBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        FavAdapter.ViewHolder viewHolder=new FavAdapter.ViewHolder(binding.getRoot());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+mList.size());
        Log.d(TAG, "onBindViewHolder: "+mList.get(position).getStep());
        switch (mList.get(position).getStep()){
            case 0:
                binding.step1.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.step2.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.step4.setVisibility(View.VISIBLE);
                break;
        }
        binding.biiName.setText(mList.get(position).getBill_name());
    }

    @Override
    public int getItemCount() {
        return null!=mList?mList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bill_name;
        private ImageView step1;
        private ImageView step2;
        private ImageView step4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bill_name=binding.biiName;
            step1=binding.step1;
            step2=binding.step2;
            step4=binding.step4;
            itemView.setOnClickListener(view -> {
                int pos=getAdapterPosition();
                if(pos!=RecyclerView.NO_POSITION){
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(view,pos);
                    }
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener=null;
    public interface OnItemClickListener{
        void OnItemClick(View v,int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

}
