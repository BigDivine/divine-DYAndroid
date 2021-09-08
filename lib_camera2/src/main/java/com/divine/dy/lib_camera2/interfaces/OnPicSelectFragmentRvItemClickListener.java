package com.divine.dy.lib_camera2.interfaces;

import android.view.View;

 import com.divine.dy.lib_camera2.select.PicBean;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public interface OnPicSelectFragmentRvItemClickListener {
    void onItemClick(View view,int position, PicBean item );
    int onItemCheckClick(View view,int position, PicBean item );
}
