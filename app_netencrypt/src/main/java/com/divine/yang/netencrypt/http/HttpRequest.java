package com.divine.yang.netencrypt.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpRequest {
    private static final String HANDSHAKE = "handshake";
    private Request.Builder request;

    // 构造 http request 用于发起请求
    public HttpRequest(String url) {
        request = new Request.Builder().url(url).get();
    }

    // 发起握手请求，目的是与服务器交换公钥
    public void handshake(Callback callback, String pubKey) {
        // 通过在header中添加handshake字段，表示当前是一个握手请求
        // 参数就是DH公钥
        request.addHeader(HANDSHAKE, pubKey);
        request(callback);
        // 握手结束后，删除header字段
        request.removeHeader(HANDSHAKE);
    }

    public void request(Callback mCallback) {
        OkHttpClient client = new OkHttpClient();
        Call mCall = client.newCall(request.build());
        mCall.enqueue(mCallback);

    }
}
