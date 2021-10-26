package com.divine.dy.module_home.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.divine.dy.lib_widget.widget.ItemDecorationGrid;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.bean.ShopHomeSubscribeBean;
import com.divine.dy.module_home.shop.listener.ShopHomeSubscribeUnfoldListener;
import com.divine.dy.module_home.shop.viewholder.ShopHomeSubscribeViewHolder;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/10/15
 * Describe:
 */
public class ShopHomeSubscribeAdapter extends RecyclerView.Adapter<ShopHomeSubscribeViewHolder> {
    private Context mContext;
    private List<ShopHomeSubscribeBean> data;
    private ShopHomeSubscribeUnfoldListener mClickListener;
    private int unfoldPosition;

    public ShopHomeSubscribeAdapter(Context mContext, List<ShopHomeSubscribeBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ShopHomeSubscribeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        ShopHomeSubscribeViewHolder vh = new ShopHomeSubscribeViewHolder(itemView);
        return vh;
    }
    int contentLineCount;
    @Override
    public void onBindViewHolder(@NonNull ShopHomeSubscribeViewHolder holder, int position) {
        ShopHomeSubscribeBean subBean = data.get(position);
        holder.tvShopHomeSubscribeTitle.setText(subBean.getTitle());
        holder.tvShopHomeSubscribeSubTitle1.setText(subBean.getSubTitle1());
        holder.tvShopHomeSubscribeSubTitle2.setText(subBean.getSubTitle2());
        int contentRepeat = new Random().nextInt(20) + 1;
        String content = subBean.getContent();
        for (int i = 0; i < contentRepeat; i++) {
            content += content;
        }
        holder.tvShopHomeSubscribeContentText.setText(content);
//        holder.tvShopHomeSubscribeContentText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                  contentLineCount = holder.tvShopHomeSubscribeContentText.getLineCount();
//
//                if (contentLineCount > 2) {
//                    holder.tvShopHomeSubscribeContentTextUnfold.setVisibility(View.VISIBLE);
//                    holder.tvShopHomeSubscribeContentText.getViewTreeObserver()
//                            .removeOnGlobalLayoutListener(this);
//                }
//            }
//        });
//        if (unfoldPosition == position && contentLineCount == 2) {
//            holder.tvShopHomeSubscribeContentText.setMaxLines(contentLineCount);
//        } else {
//            holder.tvShopHomeSubscribeContentText.setMaxLines(2);
//        }

        holder.tvShopHomeSubscribeContentTextUnfold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.unfoldClick(position);
            }
        });
        int count = subBean.getContentImg().size();
        if (count > 0) {
            int spanCount;
            if (count == 1) {
                spanCount = 1;
            } else {
                if (count % 2 == 0 && count < 5) {
                    spanCount = 2;
                } else if (count % 3 == 0) {
                    spanCount = 3;
                } else {
                    spanCount = 3;
                }
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount, RecyclerView.VERTICAL, false);
            ShopHomeSubscribeDetailAdapter adapter = new ShopHomeSubscribeDetailAdapter(mContext, subBean.getContentImg());
            holder.rvShopHomeSubscribeContentImg.setLayoutManager(gridLayoutManager);
            holder.rvShopHomeSubscribeContentImg.setAdapter(adapter);
            holder.rvShopHomeSubscribeContentImg.addItemDecoration(new ItemDecorationGrid(mContext));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setUnfoldPosition(int position) {
//        this.unfoldPosition = position;
//        notifyDataSetChanged();
    }

    public void setItemClickListener(ShopHomeSubscribeUnfoldListener mClickListener) {
//        this.mClickListener = mClickListener;
    }
}
