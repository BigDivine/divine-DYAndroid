package com.divine.dy.module_home.shop.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.divine.dy.module_home.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeRecommendFunctionViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivShopHomeRecommendFunctionViewHolderIcon;
    public TextView tvShopHomeRecommendFunctionViewHolderTitle;
    public ShopHomeRecommendFunctionViewHolder(@NonNull View itemView) {
        super(itemView);
        ivShopHomeRecommendFunctionViewHolderIcon=itemView.findViewById(R.id.item_img_text_v_middle_img);
        tvShopHomeRecommendFunctionViewHolderTitle=itemView.findViewById(R.id.item_img_text_v_middle_text);
    }
}
