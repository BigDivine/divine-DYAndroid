package com.divine.dy.lib_http.retrofit2;

import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.HttpException;

/**
 * Author: Divine
 * CreateDate: 2020/11/2
 * Describe:
 */
public class CustomException {
    public static GeneralException handleExceptions(Throwable e) {

        GeneralException GEX;
        if (e instanceof ConnectException) {
            GEX = new GeneralException(ConstOfException.NETWORK_ERROR, e.getMessage());
        } else if (e instanceof HttpException) {
            int code = ((HttpException) e).code();
            if (code == 200) {
                GEX = new GeneralException(ConstOfException.NETWORK_ERROR, e.getMessage());
            } else  if (code == 404) {
                GEX = new GeneralException(ConstOfException.NETWORK_ERROR, e.getMessage());
            } else{
                //网络错误
                ResponseBody responseBody = ((HttpException) e).response().errorBody();
                try {
                    String error = responseBody.string();
                    Log.e("aaa", error);
                    GEX = new GeneralException(ConstOfException.NETWORK_ERROR, error);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    GEX = new GeneralException(ConstOfException.NETWORK_ERROR, code + "");
                }
            }
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            GEX = new GeneralException(ConstOfException.NETWORK_ERROR, e.getMessage());
        } else if (e instanceof GeneralException) {
            GEX = new GeneralException(ConstOfException.UNKNOWN_ERROR, ((GeneralException) e).getErrorMessage());
        } else {
            //未知错误
            GEX = new GeneralException(ConstOfException.UNKNOWN_ERROR, e.getMessage());
        }
        return GEX;
    }
}
