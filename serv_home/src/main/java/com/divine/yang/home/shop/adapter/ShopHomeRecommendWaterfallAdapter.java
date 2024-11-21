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
import com.divine.yang.home.shop.bean.ShopHomeRecommendWaterfallBean;
import com.divine.yang.home.shop.viewholder.ShopHomeRecommendWaterfallViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeRecommendWaterfallAdapter extends RecyclerView.Adapter<ShopHomeRecommendWaterfallViewHolder> {
    private Context mContext;
    private List<ShopHomeRecommendWaterfallBean> data;

    public ShopHomeRecommendWaterfallAdapter(Context mContext, List<ShopHomeRecommendWaterfallBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ShopHomeRecommendWaterfallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
        // View v = LayoutInflater.from(mContext).inflate(R.layout.item_img_large, null, false);
        // ShopHomeRecommendWaterfallViewHolder vh = new ShopHomeRecommendWaterfallViewHolder(v);
        // return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHomeRecommendWaterfallViewHolder holder, int position) {
        //  Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_album_white);
        // if (position % 2 == 0) {
        //     bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        // }
        // mathImgSize(holder.ivShopHomeRecommendWaterfallViewHolderImg, bmp);
        // if (position % 2 == 0)
            // Glide.with(mContext).load(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_album_white).into(holder.ivShopHomeRecommendWaterfallViewHolderImg);
        // else
            // Glide.with(mContext).load(R.mipmap.ic_album_white).placeholder(R.mipmap.ic_album_white).into(holder.ivShopHomeRecommendWaterfallViewHolderImg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void mathImgSize(ImageView view, Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int viewWidth = DensityUtils.getScreenWidth(mContext) / 2;
        int itemHeight = viewWidth / bmpWidth * bmpHeight;
        view.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, itemHeight));
    }
}