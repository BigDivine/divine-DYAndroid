package com.divine.yang.home.shop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.divine.yang.util.sys.DensityUtils;
import com.divine.yang.home.R;
import com.divine.yang.home.shop.bean.ShopHomeRecommendEventBean;
import com.divine.yang.home.shop.viewholder.ShopHomeRecommendEventViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeRecommendEventAdapter extends RecyclerView.Adapter<ShopHomeRecommendEventViewHolder> {
    private Context mContext;
    private List<ShopHomeRecommendEventBean> data;

    public ShopHomeRecommendEventAdapter(Context mContext, List<ShopHomeRecommendEventBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ShopHomeRecommendEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
        // View v = LayoutInflater.from(mContext).inflate(R.layout.item_img_large, null, false);
        // ShopHomeRecommendEventViewHolder vh = new ShopHomeRecommendEventViewHolder(v);
        // return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHomeRecommendEventViewHolder holder, int position) {
        // Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_album_white);
        // mathImgSize(holder.ivShopHomeRecommendEventViewHolderImg, bmp);
        // Glide.with(mContext).load(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_album_white).into(holder.ivShopHomeRecommendEventViewHolderImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void mathImgSize(ImageView view, Bitmap bmp) {
        int viewWidth = DensityUtils.getScreenWidth(mContext) / 2;
        int viewHeight = DensityUtils.getScreenHeight(mContext) / 4;
        view.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, viewHeight));
    }
}
