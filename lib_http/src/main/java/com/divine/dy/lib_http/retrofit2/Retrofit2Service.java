package com.divine.dy.lib_http.retrofit2;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Author: Divine
 * CreateDate: 2020/10/29
 * Describe:
 */
public interface Retrofit2Service {
    @POST
    Observable<String> sendRequest(@Url String url, @Body RequestBody requestBody);

    @POST
    Observable<String> sendRequest(@Url String url, @Header("Authorization") String token, @Body RequestBody requestBody);

    @Streaming
    @GET
    Observable<ResponseBody> downloadRequest(@Url String url);
}
