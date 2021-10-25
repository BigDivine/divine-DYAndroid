package com.divine.dy.module_home.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendFunctionBean;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendWaterfallBean;
import com.divine.dy.module_home.shop.bean.ShopHomeSubscribeBean;
import com.divine.dy.module_home.shop.viewholder.ShopHomeSubscribeViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/10/15
 * Describe:
 */
public class ShopHomeSubscribeAdapter extends RecyclerView.Adapter<ShopHomeSubscribeViewHolder> {
    private Context mContext;
    private List<ShopHomeSubscribeBean> data;
    public ShopHomeSubscribeAdapter( Context mContext, List<ShopHomeSubscribeBean> data) {
        this.mContext = mContext;
        this.data = data;
    }
    @NonNull
    @Override
    public ShopHomeSubscribeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.item_news,parent,false);
        ShopHomeSubscribeViewHolder vh=new ShopHomeSubscribeViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHomeSubscribeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
