package com.divine.yang.http.retrofit2;

import com.divine.yang.http.AppHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Author: Divine
 * CreateDate: 2020/10/29
 * Describe:
 */
public class Retrofit2Helper {
    private static Retrofit2Helper mRetrofitHelper;
    private Retrofit2Service mRetrofit2Service;
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private Retrofit2Helper() {
        setUpOkHttp3();
        setUpRetrofit2();
    }

    public static Retrofit2Helper getInstance() {
        if (null == mRetrofitHelper) {
            synchronized (Retrofit2Helper.class) {
                if (null == mRetrofitHelper) {
                    mRetrofitHelper = new Retrofit2Helper();
                }
            }
        }
        return mRetrofitHelper;
    }

    public Retrofit2Service getService() {
        if (null == mRetrofit2Service) {
            synchronized (Retrofit2Service.class) {
                if (null == mRetrofit2Service) {
                    mRetrofit2Service = mRetrofit.create(Retrofit2Service.class);
                }
            }
        }
        return mRetrofit2Service;
    }

    private void setUpOkHttp3() {
        OkHttpClient.Builder mOkHttpBuilder = new OkHttpClient.Builder();
        mOkHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        mOkHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        mOkHttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        mOkHttpBuilder.addNetworkInterceptor(new OkHttpNetWorkInterceptor());
        //        if (!TextUtils.isEmpty(HttpBase.cerFileName)) {
        //            try {
        //                SSLSocketClient.setCertificates(mOkHttpBuilder, mContext.getAssets().open("https12306.cer"));
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        } else {
        mOkHttpBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        mOkHttpBuilder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        //        }
        mOkHttpClient = mOkHttpBuilder.build();
    }

    private void setUpRetrofit2() {
        Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder();
        mRetrofitBuilder.baseUrl(AppHttp.severUrl);
        mRetrofitBuilder.client(mOkHttpClient);
        mRetrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        mRetrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
        mRetrofit = mRetrofitBuilder.build();
    }
}
