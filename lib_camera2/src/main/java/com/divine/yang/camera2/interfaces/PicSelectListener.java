package com.divine.yang.camera2.interfaces;

import java.io.File;
import java.io.Serializable;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public interface PicSelectListener extends Serializable {

    void onSingleImageSelected(String path);

    void onMultiImageSelected(String path);

    void onMultiImageUnselected(String path);

    void onCameraShot(File imageFile);

    void onPreviewChanged(int select, int sum, boolean visible);
}

