package com.divine.dy.module_home.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendFunctionBean;
import com.divine.dy.module_home.shop.viewholder.ShopHomeRecommendFunctionViewHolder;

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
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_img_text_v_middle, null, false);
        ShopHomeRecommendFunctionViewHolder vh = new ShopHomeRecommendFunctionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHomeRecommendFunctionViewHolder holder, int position) {
        ShopHomeRecommendFunctionBean bean = data.get(position);
        holder.ivShopHomeRecommendFunctionViewHolderIcon.setColorFilter(mContext.getResources().getColor(R.color.LoginThemeColor));
//        Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.ic_album_white).into(holder.ivShopHomeRecommendFunctionViewHolderIcon);
        Glide.with(mContext).load(R.mipmap.ic_album_white).placeholder(R.mipmap.ic_album_white).into(holder.ivShopHomeRecommendFunctionViewHolderIcon);
        holder.tvShopHomeRecommendFunctionViewHolderTitle.setText(bean.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
