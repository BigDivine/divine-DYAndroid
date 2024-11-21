package com.divine.yang.camera2.select;

import android.view.View;
import android.widget.ImageView;

import com.divine.yang.camera2.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectRvViewHolder extends RecyclerView.ViewHolder {
    public ImageView mItemPicSelectRvImg,mItemPicSelectRvCheck;
    private View itemView;

    public PicSelectRvViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemPicSelectRvImg = itemView.findViewById(R.id.item_pic_select_rv_img);
        mItemPicSelectRvCheck = itemView.findViewById(R.id.item_pic_select_rv_check);
        this.itemView = itemView;
    }

    public View getItemView() {
        return this.itemView;
    }
}
