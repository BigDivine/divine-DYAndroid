package com.divine.yang.netencryptserver;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.divine.yang.netencryptrsa.AES;
import com.divine.yang.netencryptrsa.DH;
import com.divine.yang.netencryptrsa.DataUtils;
import com.divine.yang.netencryptrsa.RSA;

import java.util.Map;

import http.HttpCallBack;
import http.HttpServer;

public class Main {
    private static String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2B180nbStf0zd0i89wgVkk3Gk" +
            "RMq00QYZTMxhO3cO5vF1C+n9bapsN2u5JZX1mv8w1vZUcFPMGifdfvWel810mo80" +
            "kKQ5ficHGFZ2xMyd9y5eOxyDhmHjIK/DaEwmQaiibG+HTcI1d4Q28li7hjJXtZ+9" +
            "qLjgSkkbfHTRr8JFUwIDAQAB";
    private static String PRI_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALYHXzSdtK1/TN3S" +
            "Lz3CBWSTcaREyrTRBhlMzGE7dw7m8XUL6f1tqmw3a7kllfWa/zDW9lRwU8waJ91+" +
            "9Z6XzXSajzSQpDl+JwcYVnbEzJ33Ll47HIOGYeMgr8NoTCZBqKJsb4dNwjV3hDby" +
            "WLuGMle1n72ouOBKSRt8dNGvwkVTAgMBAAECgYEAhlsYnaiN9MWSgeQi9G2iN2NR" +
            "LRONnzrtSn1SGa6fqUMzE9awZlusv+WEQIuKjVdTZsQz241xfKQNqsKMHvwjK978" +
            "Vty+yAGY/R/CrUXPIWvLUsaUb0+7Il6oZv3/as50zwlSvew8tYBkPNwZfXvOPXJe" +
            "GmMAQy3V3S5nEEVX8SkCQQDsBDw2OWCXLrRoTcQcV3PJc/jBfv6M49ojm3jg2lhT" +
            "Qoa609Ij8OeflRX7US0c0PE3L/GeAW3XgIRTWxxVfxhdAkEAxXDtj9rMyql50c37" +
            "DC6SHjUZOFshFqvT+6FiHUUk/nksY9ckpi0E+XuX3lbqSGagsx4julh+SVnCzdGx" +
            "moI5bwJAUCzUI3t0U4c7I9+fCh2vKLUrwNeaM1RHyybKdl/V91q3GApS7YCtvmlP" +
            "0VLvJ0XXW27+/jTwEnAHA6YExwpoxQJAQbozS3ViQYEjraVtkoOaXvX8PFeR71Mq" +
            "UZVK7UWMaC58iYwghjVsd4UebITwn0OS8a3x5OH6wH5iLFRHVJFPfwJACuLjbykQ" +
            "ZT7/U6nCcXkKGo/OixC60blLHteL9FwhzNAckJPQVSpSMVLPAGw3L4xvHyFRY8RP" +
            "3JK2dJ4dT/YAlA==";

    private static final String HANDSHAKE = "handshake";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        System.out.println("start http server\r");

//        int content = 123456;
//        String encrypted = RSA.encrypt(content, pubKey);
//        System.out.println("encrypted:" + encrypted);
//        String decrypted = RSA.decrypt(encrypted, priKey);
//        System.out.println("decrypt:" + decrypted);

//        String content = "this is aes";
//        AES aes = new AES();
//        byte[] encrypted = aes.encrypt(content);
//        System.out.println("encrypted:" + new String(encrypted));
//        byte[] decrypted = aes.decrypt(encrypted);
//        System.out.println("decrypt:" + new String(decrypted));

//        DH dhC = new DH();
//        DH dhS = new DH();
//        int pubKeyC = dhC.getPubKey();
//        int pubKeyS = dhS.getPubKey();
//        byte[] secretKeyC = dhC.getSecretKey(pubKeyS);
//        byte[] secretKeyS = dhS.getSecretKey(pubKeyC);
//        System.out.println("secretKeyC is :" + new String(secretKeyC));
//        System.out.println("secretKeyS is :" + new String(secretKeyS));

        // 创建一个socket
        HttpServer server = new HttpServer(new HttpCallBack() {
            private DH dh = new DH();
            private AES aes = new AES();

            @Override
            public byte[] onResponse(String request) {
                System.out.printf("onResponse\r");
                //判断是否为握手请求
                if (isHandshake(request)) {
                    //握手请求
                    Map<String, String> header = HttpServer.getHeader(request);
                    String handshake = header.get(HANDSHAKE);
                    System.out.println("handshake from Client is :" + handshake);
                    //拿到客户端的公钥
                    int dhPublicKey = Integer.parseInt(RSA.decrypt(handshake, PRI_KEY));
                    //接收header中的"handshake"内容（公钥）,设置aes的密钥
                    aes.setSecretKey(dh.getSecretKey(dhPublicKey));

                    int pubKey = dh.getPubKey();
                    return DataUtils.int2Byte(pubKey);
                } else {
                    //应用请求操作

                    return aes.encrypt("this is a all action of encrypt");
                }
            }
        });

        server.startHttpServer();
    }

    /**
     * 通过Header中的"handshake"字段判断是否为握手请求
     *
     * @param request 客户端request
     * @return 是否为握手包
     */
    private static boolean isHandshake(String request) {
        if (request != null && request.contains(HANDSHAKE)) {
            return true;
        }
        return false;
    }
}
