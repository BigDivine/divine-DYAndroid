package com.divine.dy.lib_camera2.interfaces;

import android.view.View;

 import com.divine.dy.lib_camera2.select.PicBean;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public interface OnPicSelectImageClickListener {
    void onCameraClick(PicBean item );
    void onItemClick(View view,int position, PicBean item );
    void onItemCheck(View view,int position, PicBean item );
}
