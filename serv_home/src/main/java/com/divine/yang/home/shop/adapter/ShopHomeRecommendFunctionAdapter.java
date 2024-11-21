package com.divine.yang.home.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.divine.yang.home.R;
import com.divine.yang.home.shop.bean.ShopHomeRecommendFunctionBean;
import com.divine.yang.home.shop.viewholder.ShopHomeRecommendFunctionViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeRecommendFunctionAdapter extends RecyclerView.Adapter<ShopHomeRecommendFunctionViewHolder> {
    private Context mContext;
    private List<ShopHomeRecommendFunctionBean> data;

    public ShopHomeRecommendFunctionAdapter(Context mContext, List<ShopHomeRecommendFunctionBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ShopHomeRecommendFunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
        // View v = LayoutInflater.from(mContext).inflate(R.layout.item_img_text_v_middle, null, false);
        // ShopHomeRecommendFunctionViewHolder vh = new ShopHomeRecommendFunctionViewHolder(v);
        // return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHomeRecommendFunctionViewHolder holder, int position) {
        ShopHomeRecommendFunctionBean bean = data.get(position);
        // Glide.with(mContext).load(bean.getFunctionImg()).placeholder(R.mipmap.ic_album_white).into(holder.ivShopHomeRecommendFunctionViewHolderIcon);
        holder.tvShopHomeRecommendFunctionViewHolderTitle.setText(bean.getFunctionTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<ShopHomeRecommendFunctionBean> data) {
        this.data.clear();
        this.data.addAll(data);
        //        this.notify();
        this.notifyDataSetChanged();
    }
}
