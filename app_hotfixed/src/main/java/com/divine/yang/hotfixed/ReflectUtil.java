package com.divine.yang.hotfixed;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * 获取某个字段或者方法
 */
public class ReflectUtil {

    /**
     * 获取某个字段
     *
     * @param instance 对象实例
     * @param name     字段名称
     * @return 对应的字段field
     */
    public static Field findField(Object instance, String name) throws NoSuchFieldException {

        Class clazz = instance.getClass();
        Field field = null;
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("no such field:" + name);
    }

    /**
     * 获取某个方法
     *
     * @param instance       实例对象
     * @param name           方法名
     * @param parameterTypes 方法参数列表
     * @return 对应的方法
     */
    public static Method findMethod(Object instance, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Log.e("dexPathList method-" + i, methods[i].getName());
        }
        while (clazz != null) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchMethodException("no such method:" + name);

    }
}
