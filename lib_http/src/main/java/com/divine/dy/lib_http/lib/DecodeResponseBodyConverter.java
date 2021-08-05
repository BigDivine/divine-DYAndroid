package com.divine.dy.lib_http.lib;

import com.divine.dy.lib_http.retrofit2.GeneralException;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * author: Divine
 *
 * <p>
 * date: 2018/12/18
 */
public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            //解密字符串
            String temp = new String(value.bytes(), StandardCharsets.UTF_8);
            JSONObject object = null;
            try {
                object = new JSONObject(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String data = object.optString("data");
            if ("".equals(data)) {
                int code = object.optInt("code");
                String message = object.optString("msg");
                if (code == 0) {
                    return adapter.fromJson(object.toString());
                } else {
                    throw new GeneralException(code, message);
                }
            } else {
                return adapter.fromJson(object.toString());
            }
        } finally {
            value.close();
        }
    }
}