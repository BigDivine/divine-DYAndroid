package com.divine.yang.githubdemo.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.githubdemo.R;


public class MainRecyclerVH extends RecyclerView.ViewHolder {
    public TextView title;

    public MainRecyclerVH(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.vh_main);
    }
}
