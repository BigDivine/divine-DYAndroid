package com.divine.yang.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.main.R;
import com.divine.yang.main.bean.HomeBean;
import com.divine.yang.main.enums.LISTTYPE;
import com.divine.yang.main.listener.HomeItemClickListener;
import com.divine.yang.main.viewholder.HomeViewHolder;

import java.util.ArrayList;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main.adapter
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/12/4
 * Description   :
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private Context mContext;
    private int listType;
    private ArrayList<HomeBean> homeBeans;
    private HomeItemClickListener mHomeItemClickListener;

    public HomeAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.listType = type;
        homeBeans = new ArrayList<>();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int viewId = -1;
        if (listType == LISTTYPE.LIST.ordinal()) {
            // 列表流形式
            viewId = R.layout.recycler_item_home_list;
        } else if (listType == LISTTYPE.FALL.ordinal()) {
            // 瀑布流形式
            viewId = R.layout.recycler_item_home_fall;
        } else if (listType == LISTTYPE.CARD.ordinal()) {
            // 卡片流形式
            viewId = R.layout.recycler_item_home_card;
        }
        if (viewId != -1) {
            View view = LayoutInflater.from(mContext).inflate(viewId, parent, false);
            HomeViewHolder vh = new HomeViewHolder(view, listType);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        if (!homeBeans.isEmpty()) {
            HomeBean curHomeBean = homeBeans.get(position);
            holder.update(curHomeBean);
            holder.itemView.setOnClickListener(v -> {
                mHomeItemClickListener.onItemClick(v, curHomeBean);
            });
        }
    }

    @Override
    public int getItemCount() {
        return homeBeans.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<HomeBean> homeBeans) {
        this.homeBeans = homeBeans;
        notifyDataSetChanged();
    }

    public void setHomeItemClickListener(HomeItemClickListener mHomeItemClickListener) {
        this.mHomeItemClickListener = mHomeItemClickListener;
    }
}
