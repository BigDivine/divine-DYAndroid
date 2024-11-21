package com.divine.yang.camera2;

import android.util.Size;

import com.divine.yang.util.sys.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: Divine
 * CreateDate: 2021/9/7
 * Describe:
 */
public class CameraUtils {
    //拍照图片文件名称
    private static String camera2PicSourceName;
    //裁剪后图片文件名称
    private static String camera2PicCropName;

    public static String getCamera2PicSourceName() {
        return camera2PicSourceName;
    }

    public static void setCamera2PicSourceName() {
        camera2PicSourceName = "Camera2_" + FileUtils.makeFileName("png");
    }

    public static String getCamera2PicCropName() {
        return camera2PicCropName;
    }

    public static void setCamera2PicCropName() {
        camera2PicCropName = "Crop_" + FileUtils.makeFileName("png");
    }

    /**
     * 获取最合适的图片预览和保存的尺寸
     *
     * @param exchange 是否需要交换宽高
     * @param sizeList 设备支持的尺寸集合
     * @return
     */
    public static Size getBestSize(boolean exchange, List<Size> sizeList) {
        List<Size> enoughSize = new ArrayList<>();
        for (Size size : sizeList) {
            float di = (float) size.getWidth() / (float) size.getHeight();
            if (exchange) {
                                if (di == (16.0f / 9.0f)) {
//                if (di == (4.0f / 3.0f)) {
                    enoughSize.add(size);
                }
            } else {
                                 if (di  == (9.0f / 16.0f)) {
//                if (di == (3.0f / 4.0f)) {
                    enoughSize.add(size);
                }
            }
        }
        if (null != enoughSize && enoughSize.size() > 0) {
            return Collections.max(enoughSize, new CompareSizesByArea());
        } else {
            return null;
        }
    }

    public static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size size1, Size size2) {
            return Long.signum(size1.getWidth() * size1.getHeight() - size2.getWidth() * size2.getHeight());
        }
    }
}
