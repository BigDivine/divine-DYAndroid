package com.divine.yang.hotfixed;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责运行时注入补丁包
 */
public class HotFixManager {
    public static final String FIXED_DEX_SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/fixed.dex";

    /**
     * 注入补丁包
     * 利用反射技术实现，将新的dex放入pathList->dexElements的第一个位置
     * @param context
     */
    public static void installFixedDex(Context context) {
        try {
            File fixedDexFile = new File(FIXED_DEX_SDCARD_PATH);
            if (!fixedDexFile.exists()) {
                return;
            }
            Field pathListField = ReflectUtil.findField(context.getClassLoader(), "pathList");
            Object dexPathList = pathListField.get(context.getClassLoader());

            Method makeDexElements = ReflectUtil.findMethod(dexPathList, "makeDexElements", List.class, File.class, List.class, ClassLoader.class);

            ArrayList<File> filesToBeInstalled = new ArrayList<>();
            filesToBeInstalled.add(fixedDexFile);

            File optimizedDirectory = new File(context.getFilesDir(), "fixed_dex");

            ArrayList<IOException> suppressedException = new ArrayList<>();

            Object[] extraElements = (Object[]) makeDexElements.invoke(dexPathList, filesToBeInstalled, optimizedDirectory, suppressedException, context.getClassLoader());
//                    , context.getClassLoader());
            //获取当前的elements数组
            Field dexElementsField = ReflectUtil.findField(dexPathList, "dexElements");
            Object[] originElements = (Object[]) dexElementsField.get(dexPathList);
            //创建一个新的数组，类型为originElements的类型，长度为origin和extra两个数组长度之和
            Object[] combinedElements = (Object[]) Array.newInstance(originElements.getClass().getComponentType(), originElements.length + extraElements.length);
            //拷贝两个数组到一个新的数组
            System.arraycopy(extraElements, 0, combinedElements, 0, extraElements.length);
            System.arraycopy(originElements, 0, combinedElements, extraElements.length, originElements.length);

            //将新的combinedElements，重新复制给dexPathList
            //替换原来的dexElements数组
            dexElementsField.set(dexPathList, combinedElements);
            //通过反射替换变量完成
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
