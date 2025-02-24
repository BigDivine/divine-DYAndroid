package com.divine.yang.splash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/3/1
 * Describe:
 */
public class SplashAdapter extends RecyclerView.Adapter<SplashHolder> {
    private final Context mContext;
    private final List<Integer> data;
    private OnSplashItemClickListener listener;

    public SplashAdapter(Context mContext, List<Integer> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public SplashHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.splash_item, parent, false);
        return new SplashHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SplashHolder holder, int position) {
        holder.image.setImageResource(data.get(position));
        if (position == getItemCount() - 1) {
            holder.image.setOnClickListener(view -> {
                listener.onLastItemClick(view);
            });
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnSplashItemClickListener(OnSplashItemClickListener listener) {
        this.listener = listener;
    }
}
