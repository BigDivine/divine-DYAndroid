package com.divine.dy.lib_camera2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;

import com.divine.dy.lib_utils.sys.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Author: Divine
 * CreateDate: 2021/9/8
 * Describe:
 */
public class Camera2Utils {
    /**
     * @param reader ImageReader
     * @param picDir 文件保存目录，不包括filename
     * @throws IOException
     */
    public static void saveCamera2Pic(ImageReader reader, String picDir) throws IOException {
        File file = new File(picDir + CameraUtils.getCamera2PicSourceName());
        FileUtils.createFile(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Image image = reader.acquireNextImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        //将image对象转化为byte，再转化为bitmap
        buffer.get(bytes);
        Bitmap bitmapSource = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmapSource != null) {
            // 解决异常-java.lang.IllegalStateException: maxImages (7) has already been acquired, call #close before acquiring more.
            bitmapSource.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
            // 释放image资源
            image.close();
        } else {
            Log.e("DY-camera2", "图片保存失败");
        }
    }

    /**
     * 获取图片应该旋转的角度，使图片竖直
     *
     * @param c
     * @param deviceOrientation
     * @return
     */
    public static int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        if (deviceOrientation ==  OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);

        deviceOrientation = (deviceOrientation + 45) / 90 * 90;
        // LENS_FACING相对于设备屏幕的方向,LENS_FACING_FRONT相机设备面向与设备屏幕相同的方向
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront)
            deviceOrientation = -deviceOrientation;
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;
        return jpegOrientation;
    }

    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     *
     * @param displayRotation
     * @param sensorOrientation
     * @return
     */
    public static boolean exchangeWidthAndHeight(int displayRotation, int sensorOrientation) {
        boolean exchange = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true;
                }
                break;
            case Surface.ROTATION_90:
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true;
                    break;
                }
            case Surface.ROTATION_270:
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true;
                    break;
                }
        }
        return exchange;
    }
}
