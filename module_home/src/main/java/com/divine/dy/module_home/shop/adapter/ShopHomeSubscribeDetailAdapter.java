package com.divine.dy.module_home.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.viewholder.ShopHomeSubscribeDetailViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/10/15
 * Describe:
 */
public class ShopHomeSubscribeDetailAdapter extends RecyclerView.Adapter<ShopHomeSubscribeDetailViewHolder> {
    private Context mContext;
    private List<String> data;

    public ShopHomeSubscribeDetailAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ShopHomeSubscribeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_img_large, parent, false);
        ShopHomeSubscribeDetailViewHolder vh = new ShopHomeSubscribeDetailViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHomeSubscribeDetailViewHolder holder, int position) {
        String subDetailImg = data.get(position);

        Glide.with(mContext).load(R.mipmap.ic_motion_18).placeholder(R.mipmap.ic_default_img_grey_alpha).into(holder.ivShopHomeSubscribeDetailIcon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
