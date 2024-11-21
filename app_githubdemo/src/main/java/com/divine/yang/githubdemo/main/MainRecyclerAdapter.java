package com.divine.yang.githubdemo.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.githubdemo.R;
import com.divine.yang.githubdemo.RecyclerItemClickListener;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerVH> {
    private Context mContext;
    private ArrayList<String> data;
    private RecyclerItemClickListener mClickListener;

    public MainRecyclerAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MainRecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vhView = LayoutInflater.from(mContext).inflate(R.layout.vh_main, parent, false);
        MainRecyclerVH mrvh = new MainRecyclerVH(vhView);
        return mrvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerVH holder, int position) {
        int p = position;
        String title = data.get(position);
        holder.title.setText(title);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view, p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size() != 0 ? data.size() : 0;
    }

    public void setItemClickListener(RecyclerItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }
}
