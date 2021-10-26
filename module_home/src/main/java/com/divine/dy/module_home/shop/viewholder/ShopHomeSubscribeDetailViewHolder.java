package com.divine.dy.module_home.shop.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.divine.dy.module_home.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/10/15
 * Describe:
 */
public class ShopHomeSubscribeDetailViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivShopHomeSubscribeDetailIcon;

    public ShopHomeSubscribeDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        ivShopHomeSubscribeDetailIcon = itemView.findViewById(R.id.item_img_large_img);
    }
}
