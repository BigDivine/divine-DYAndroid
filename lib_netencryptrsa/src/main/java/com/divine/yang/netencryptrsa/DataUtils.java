package com.divine.yang.netencryptrsa;

import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;

public class DataUtils {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String base64Encode(byte[] data) {
        // 功能及接口设计与解码类似
        try {
            return Base64.encodeToString(data, Base64.NO_WRAP);

        } catch (Exception e) {
            return java.util.Base64.getMimeEncoder().encodeToString(data);
        }
    }

    // 传入base64编码后的字符串，返回解码后的byte[]
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] base64Decode(String data) {
        // java类中提供了base64（不太好用）
        // android类中也提供了base64（简单易用）
        try {
            // android端使用android sdk中的base64
            return Base64.decode(data, Base64.NO_WRAP);

        } catch (Exception e) {
            // 运行过程中找不到该函数，进入catch
            // 则表示在服务端运行
            return java.util.Base64.getMimeDecoder().decode(data);
        }
    }

    // 将int转换为byte数组，一个int对应内存为4byte
    public static byte[] int2Byte(int data) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(data);
        return byteBuffer.array();
    }

    // 将byte数组转换为int
    public static int byte2Int(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        return byteBuffer.getInt();
    }
}
