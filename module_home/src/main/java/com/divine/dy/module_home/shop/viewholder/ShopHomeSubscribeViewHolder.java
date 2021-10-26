package com.divine.dy.module_home.shop.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.divine.dy.lib_widget.widget.CircleImageView;
import com.divine.dy.module_home.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/10/15
 * Describe:
 */
public class ShopHomeSubscribeViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView civShopHomeSubscribeIcon;
    public LinearLayout llShopHomeSubscribeAdd;
    public TextView tvShopHomeSubscribeTitle, tvShopHomeSubscribeSubTitle1, tvShopHomeSubscribeSubTitle2, tvShopHomeSubscribeContentText, tvShopHomeSubscribeContentTextUnfold;
    public RecyclerView rvShopHomeSubscribeContentImg;

    public ShopHomeSubscribeViewHolder(@NonNull View itemView) {
        super(itemView);
        civShopHomeSubscribeIcon = itemView.findViewById(R.id.item_news_icon);
        llShopHomeSubscribeAdd = itemView.findViewById(R.id.item_news_add_btn);
        tvShopHomeSubscribeTitle = itemView.findViewById(R.id.item_news_title);
        tvShopHomeSubscribeSubTitle1 = itemView.findViewById(R.id.item_news_sub_title_1);
        tvShopHomeSubscribeSubTitle2 = itemView.findViewById(R.id.item_news_sub_title_2);
        tvShopHomeSubscribeContentText = itemView.findViewById(R.id.item_news_content_text);
        tvShopHomeSubscribeContentTextUnfold = itemView.findViewById(R.id.item_news_content_text_unfold);
        rvShopHomeSubscribeContentImg = itemView.findViewById(R.id.item_news_content_img);
    }
}
