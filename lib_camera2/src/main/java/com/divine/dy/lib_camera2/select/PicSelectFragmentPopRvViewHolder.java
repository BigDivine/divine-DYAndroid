package com.divine.dy.lib_camera2.select;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.divine.dy.lib_camera2.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectFragmentPopRvViewHolder extends RecyclerView.ViewHolder {
    public ImageView mAdapterPicSelectFragmentPopRvItemImg, mAdapterPicSelectFragmentPopRvItemSelect;
    public TextView mAdapterPicSelectFragmentPopRvItemTitle, mAdapterPicSelectFragmentPopRvItemNum;
    private View itemView;

    public PicSelectFragmentPopRvViewHolder(@NonNull View itemView) {
        super(itemView);
        mAdapterPicSelectFragmentPopRvItemImg = itemView.findViewById(R.id.adapter_pic_select_fragment_pop_rv_item_img);
        mAdapterPicSelectFragmentPopRvItemSelect = itemView.findViewById(R.id.adapter_pic_select_fragment_pop_rv_item_select);
        mAdapterPicSelectFragmentPopRvItemTitle = itemView.findViewById(R.id.adapter_pic_select_fragment_pop_rv_item_title);
        mAdapterPicSelectFragmentPopRvItemNum = itemView.findViewById(R.id.adapter_pic_select_fragment_pop_rv_item_num);
        this.itemView = itemView;
    }

    public View getItemView() {
        return this.itemView;
    }
}
