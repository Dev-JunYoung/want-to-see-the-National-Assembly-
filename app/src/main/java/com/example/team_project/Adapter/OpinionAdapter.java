package com.example.team_project.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.databinding.ItemOpinionBinding;

import java.util.ArrayList;

public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.ViewHolder> {
    private final String TAG=this.getClass().getSimpleName();
    private ItemOpinionBinding binding;
    private ArrayList<OpinionData> mList;

    public OpinionAdapter(ArrayList<OpinionData> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ItemOpinionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        ViewHolder viewHolder=new ViewHolder(binding.getRoot());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        binding.tvContent.setText(mList.get(position).getContent());
        binding.tvRegName.setText(mList.get(position).getRegName());
        binding.tvRegDate.setText(mList.get(position).getRegDate());

    }

    @Override
    public int getItemCount() {
        return null!=mList?mList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
    private RowAdapter.OnItemClickListener onItemClickListener=null;
    public interface OnItemClickListener{
        void OnItemClick(View v,int pos);
    }
    public void setOnItemClickListener(RowAdapter.OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
