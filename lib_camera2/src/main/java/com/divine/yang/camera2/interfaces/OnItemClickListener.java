package com.divine.yang.camera2.interfaces;

import com.divine.yang.camera2.select.PicBean;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public interface OnItemClickListener {

    int onCheckedClick(int position, PicBean image);

    void onImageClick(int position, PicBean image);
}

