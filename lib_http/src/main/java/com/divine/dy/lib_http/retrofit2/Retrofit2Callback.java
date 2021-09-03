package com.divine.dy.lib_http.retrofit2;

/**
 * Author: Divine
 * CreateDate: 2021/2/25
 * Describe:
 */
public interface Retrofit2Callback<T> {
    void onSuccess(T response);

    void onFailed(String msg);
}
