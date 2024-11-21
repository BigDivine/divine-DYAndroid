package com.divine.yang.netencryptrsa;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private static final String RSA_ALGOROTHM = "RSA/ECB/PKCS1Padding";
    // 加密函数，传入明文、公钥；返回密文

    /**
     * @param data   类型自定，以int为例
     * @param pubKey
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(int data, String pubKey) {
        String message = String.valueOf(data);
        byte[] decoded = DataUtils.base64Decode(pubKey);
        byte[] result = new byte[]{0};
        try {
            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA_ALGOROTHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            result = cipher.doFinal(message.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return DataUtils.base64Encode(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String encrypted, String priKey) {
        byte[] decoded = DataUtils.base64Decode(priKey);
        byte[] content = DataUtils.base64Decode(encrypted);

        byte[] result = new byte[]{0};

        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA_ALGOROTHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = cipher.doFinal(content);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return new String(result);
    }
}
