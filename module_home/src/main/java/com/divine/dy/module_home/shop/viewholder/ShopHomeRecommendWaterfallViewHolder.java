package com.divine.dy.module_home.shop.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.divine.dy.module_home.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeRecommendWaterfallViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivShopHomeRecommendWaterfallViewHolderImg;

    public ShopHomeRecommendWaterfallViewHolder(@NonNull View itemView) {
        super(itemView);
        ivShopHomeRecommendWaterfallViewHolderImg = itemView.findViewById(R.id.item_img_large_img);
    }

}
