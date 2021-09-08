//package com.divine.dy.lib_utils;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * Author: Divine
// * CreateDate: 2021/7/1
// * Describe:
// */
//public class ImgCompressUtils {
//    /***
//     * 以下是我们图片压缩库的基本的算法
//     *
//     * 下面的压缩算法的最终结果是：
//     * 1).短边在 1000~2000 之间（经过设置相机的参数，通常短边取值是1080、1200等值）
//     * 2).压缩的质量为 75%
//     *
//     * @return
//     * @throws IOException
//     */
//   public File compress() throws IOException {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        // 这里我们通过我们的计算算法来得到一个采样率
//        options.inSampleSize = myComputeSize();
//
//        Bitmap tagBitmap = BitmapFactory.decodeStream(srcImg.open(), null, options);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        if (Checker.SINGLE.isJPG(srcImg.open())) {
//            tagBitmap = rotatingImage(tagBitmap, Checker.SINGLE.getOrientation(srcImg.open()));
//        }
//        // 这里使用了固定的压缩比75，可以通过为构建者模式增加字段来修改库，动态为其赋值
//        tagBitmap.compress(focusAlpha ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 75, stream);
//        tagBitmap.recycle();
//
//        FileOutputStream fos = new FileOutputStream(tagImg);
//        fos.write(stream.toByteArray());
//        fos.flush();
//        fos.close();
//        stream.close();
//
//        return tagImg;
//    }
//
//
//    /**
//     * 这里的算法是以短边压缩到1000~2000之间为目标，通过计算到1000的比值，然后需要将采样率控制为2的倍数
//     * 所以需要使用方法{@link #calInSampleSize(int)}进行计算
//     *
//     * @return 采样率
//     */
//    private int myComputeSize() {
//        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
//        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
//
//        int shortSide = Math.min(srcWidth, srcHeight);
//
//        int rate = (int) Math.floor(shortSide / 1000.0);
//
//        return calInSampleSize(rate);
//    }
//
//    /**
//     * 通过移位操作计算采样率，是某个整数对应的二进制数保留最高位为1，其他位置为0的结果
//     *
//     * @param rate 比例
//     * @return 采样率
//     */
//    private int calInSampleSize(int rate) {
//        int i = 0;
//        while ((rate >> (++i)) != 0) ;
//        return 1 << --i;
//    }
//}
