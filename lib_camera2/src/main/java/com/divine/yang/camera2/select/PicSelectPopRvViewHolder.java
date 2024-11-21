package com.divine.yang.camera2.select;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.divine.yang.camera2.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectPopRvViewHolder extends RecyclerView.ViewHolder {
    public ImageView mPicSelectRvItemImg, mPicSelectRvItemChecked;
    public TextView mPicSelectRvItemName;
    private View itemView;

    public PicSelectPopRvViewHolder(@NonNull View itemView) {
        super(itemView);
        mPicSelectRvItemImg = itemView.findViewById(R.id.item_pic_select_folder_img);
        mPicSelectRvItemName = itemView.findViewById(R.id.item_pic_select_folder_name);
        mPicSelectRvItemChecked = itemView.findViewById(R.id.item_pic_select_folder_checked);
        this.itemView = itemView;
    }

    public View getItemView() {
        return this.itemView;
    }
}
