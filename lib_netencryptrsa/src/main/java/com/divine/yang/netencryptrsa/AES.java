package com.divine.yang.netencryptrsa;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private SecretKey secretKey;

    public AES() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            // 随机生成密码，并设置种子
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(System.currentTimeMillis());

            keyGenerator.init(128, secureRandom);

            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public AES(byte[] key) {
        secretKey = new SecretKeySpec(key, "AES");
    }

    public byte[] getSecretKey() {
        return secretKey.getEncoded();
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = new SecretKeySpec(secretKey, "AES");
    }

    public byte[] encrypt(String content) {
        if (secretKey == null) {
            // 没有生成密钥
            return new byte[]{-1};
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(content.getBytes());

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
        return new byte[]{-1};
    }

    public byte[] decrypt(byte[] encrypted) {
        if (secretKey == null) {
            return new byte[]{-1};
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encrypted);
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
        return new byte[]{-1};
    }
}
