package com.divine.yang.camera2.interfaces;

import com.divine.yang.camera2.select.PicFolderBean;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public interface OnFolderChangeListener {

    void onChange(int position, PicFolderBean folder);
}