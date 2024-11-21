package com.divine.yang.netencryptrsa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DH {
    private static int dhP = 23;
    private static int dhG = 5;

    private int privateKey;

    // 构造器中，初始化私钥
    public DH() {
        Random random = new Random();
        privateKey = random.nextInt(10);
        System.out.println("dh private key is :" + privateKey);
    }

    // 使用公钥公式计算出自己对公钥
    public int getPubKey() {
        return (int) Math.pow(dhG, privateKey) % dhP;
    }

    // 接受对方对公钥，结合自己对私钥产生密钥
    public byte[] getSecretKey(int publicKey) {
        int data = (int) Math.pow(publicKey, privateKey) % dhP;
        return sha256(data);
    }

    // 将dh交换之后生成的密钥，进行一个hash的计算
    // 目的是转换成byte[128]类型，用作AES的密钥
    private byte[] sha256(int data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            messageDigest.update(DataUtils.int2Byte(data));

            return messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[]{-1};
    }
}
