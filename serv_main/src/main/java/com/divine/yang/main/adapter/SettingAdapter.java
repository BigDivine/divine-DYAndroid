package com.divine.yang.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.main.R;
import com.divine.yang.main.bean.SettingBean;
import com.divine.yang.main.viewholder.SettingViewHolder;

import java.util.ArrayList;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main.adapter
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/12/4
 * Description   :
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {
    private Context mContext;
    private ArrayList<SettingBean> settingBeans;

    public SettingAdapter(Context mContext) {
        this.mContext = mContext;
        settingBeans = new ArrayList<>();
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_home_list, parent, false);
        SettingViewHolder vh = new SettingViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        if (!settingBeans.isEmpty()) {
            holder.update(settingBeans.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return settingBeans.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<SettingBean> settingBeans) {
        this.settingBeans = settingBeans;
        notifyDataSetChanged();
    }
}
