package com.divine.yang.netencrypt;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.divine.yang.netencrypt.http.HttpRequest;
import com.divine.yang.netencryptrsa.AES;
import com.divine.yang.netencryptrsa.DH;
import com.divine.yang.netencryptrsa.DataUtils;
import com.divine.yang.netencryptrsa.RSA;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2B180nbStf0zd0i89wgVkk3Gk" +
            "RMq00QYZTMxhO3cO5vF1C+n9bapsN2u5JZX1mv8w1vZUcFPMGifdfvWel810mo80" +
            "kKQ5ficHGFZ2xMyd9y5eOxyDhmHjIK/DaEwmQaiibG+HTcI1d4Q28li7hjJXtZ+9" +
            "qLjgSkkbfHTRr8JFUwIDAQAB";

    private String url = "http://192.168.1.13";
    private final String TAG = "yangzelong";

    private byte[] mAesKey;

    private AES aes = new AES();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final HttpRequest httpRequest = new HttpRequest(url);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            DH dh = new DH();

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                通过判断aeskey是否可用，来确定是否需要握手
                // 如果aes不可用，则进行重新握手
                if (mAesKey == null || mAesKey.length <= 0) {
                    int pubKey = dh.getPubKey();
                    Log.d(TAG, "public key is :" + dh.getPubKey());

                    httpRequest.handshake(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(TAG, "handshake请求失败");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                            response.body().string()中的 (.string)只能使用一次--打开下面注释会报错
//                            Log.e(TAG, "handshake请求成功：" + response.body().string());
                            byte[] pubKey = response.body().bytes();
                            mAesKey = dh.getSecretKey(DataUtils.byte2Int(pubKey));
                            Log.d(TAG, "aes key from server is :=" + new String(mAesKey));

                        }
                    }, RSA.encrypt(pubKey, PUB_KEY));
                } else {
                    httpRequest.request(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(TAG, "Get请求失败");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            byte[] content = response.body().bytes();
                            AES aes = new AES(mAesKey);

                            Log.e(TAG, "Get请求成功：" + new String(aes.decrypt(content)));

                        }
                    });
                }

            }
        });
    }
}
