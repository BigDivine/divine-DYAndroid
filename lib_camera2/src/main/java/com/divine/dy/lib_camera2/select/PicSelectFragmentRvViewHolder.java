package com.divine.dy.lib_camera2.select;

import android.view.View;
import android.widget.ImageView;

import com.divine.dy.lib_camera2.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectFragmentRvViewHolder extends RecyclerView.ViewHolder {
    public ImageView mAdapterPicSelectFragmentRvItemImg,mAdapterPicSelectFragmentRvItemCheck;
    private View itemView;

    public PicSelectFragmentRvViewHolder(@NonNull View itemView) {
        super(itemView);
        mAdapterPicSelectFragmentRvItemImg = itemView.findViewById(R.id.adapter_pic_select_fragment_rv_item_img);
        mAdapterPicSelectFragmentRvItemCheck = itemView.findViewById(R.id.adapter_pic_select_fragment_rv_item_check);
        this.itemView = itemView;
    }

    public View getItemView() {
        return this.itemView;
    }
}
