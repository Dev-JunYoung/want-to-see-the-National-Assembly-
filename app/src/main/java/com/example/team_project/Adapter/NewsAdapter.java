package com.example.team_project.Adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.databinding.ItemNewsBinding;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final String TAG=this.getClass().getSimpleName();
    private ArrayList<NewsData> mList;
    private ItemNewsBinding binding;
    public NewsAdapter(ArrayList<NewsData> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        ViewHolder viewHolder=new ViewHolder(binding.getRoot());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(Html.fromHtml(mList.get(position).getTitle()));
        holder.tv_description.setText(Html.fromHtml(mList.get(position).getDescription()));
    }

    @Override
    public int getItemCount() {
        return null!=mList?mList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=binding.tvTitle;
            tv_description=binding.tvDescription;
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
