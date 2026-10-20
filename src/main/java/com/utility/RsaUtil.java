package com.utility;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 加解密工具类（JDK 8）
 * <p>
 * 算法：RSA/ECB/PKCS1Padding。<br>
 * 常用于加密传输 AES 密钥（混合加密），不建议直接加密大段业务数据。
 */
public final class RsaUtil {

    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final int DEFAULT_KEY_SIZE = 2048;

    private RsaUtil() {
    }

    /**
     * 生成 RSA 密钥对（默认 2048 位）
     *
     * @return [0]=公钥 Base64，[1]=私钥 Base64
     */
    public static String[] generateKeyPair() {
        return generateKeyPair(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成 RSA 密钥对
     *
     * @param keySize 推荐 2048
     * @return [0]=公钥 Base64，[1]=私钥 Base64
     */
    public static String[] generateKeyPair(int keySize) {
        if (keySize < 1024) {
            throw new IllegalArgumentException("RSA 密钥长度不能小于 1024");
        }
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(keySize, new SecureRandom());
            KeyPair keyPair = generator.generateKeyPair();
            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            return new String[]{publicKey, privateKey};
        } catch (Exception e) {
            throw new IllegalArgumentException("生成 RSA 密钥对失败", e);
        }
    }

    /**
     * Base64 公钥转 PEM（便于前端 JSEncrypt 使用）
     */
    public static String toPublicKeyPem(String publicKeyBase64) {
        return toPem("PUBLIC KEY", publicKeyBase64);
    }

    /**
     * Base64 私钥转 PEM
     */
    public static String toPrivateKeyPem(String privateKeyBase64) {
        return toPem("PRIVATE KEY", privateKeyBase64);
    }

    /**
     * 公钥加密，返回 Base64 密文
     */
    public static String encryptByPublicKey(String plainText, String publicKeyBase64) {
        if (plainText == null) {
            throw new IllegalArgumentException("明文不能为空");
        }
        byte[] encrypted = encryptByPublicKey(plainText.getBytes(StandardCharsets.UTF_8), publicKeyBase64);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] plainBytes, String publicKeyBase64) {
        checkBytes(plainBytes, "明文");
        try {
            PublicKey publicKey = toPublicKey(publicKeyBase64);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainBytes);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 公钥加密失败", e);
        }
    }

    /**
     * 私钥解密 Base64 密文
     */
    public static String decryptByPrivateKey(String cipherText, String privateKeyBase64) {
        if (cipherText == null || cipherText.trim().isEmpty()) {
            throw new IllegalArgumentException("密文不能为空");
        }
        byte[] plain = decryptByPrivateKey(Base64.getDecoder().decode(cipherText.trim()), privateKeyBase64);
        return new String(plain, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] cipherBytes, String privateKeyBase64) {
        checkBytes(cipherBytes, "密文");
        try {
            PrivateKey privateKey = toPrivateKey(privateKeyBase64);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherBytes);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 私钥解密失败", e);
        }
    }

    /**
     * 私钥加密（少见，多用于签名场景的兼容加密）
     */
    public static String encryptByPrivateKey(String plainText, String privateKeyBase64) {
        if (plainText == null) {
            throw new IllegalArgumentException("明文不能为空");
        }
        try {
            PrivateKey privateKey = toPrivateKey(privateKeyBase64);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 私钥加密失败", e);
        }
    }

    /**
     * 公钥解密
     */
    public static String decryptByPublicKey(String cipherText, String publicKeyBase64) {
        if (cipherText == null || cipherText.trim().isEmpty()) {
            throw new IllegalArgumentException("密文不能为空");
        }
        try {
            PublicKey publicKey = toPublicKey(publicKeyBase64);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] plain = cipher.doFinal(Base64.getDecoder().decode(cipherText.trim()));
            return new String(plain, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 公钥解密失败", e);
        }
    }

    private static PublicKey toPublicKey(String publicKeyBase64) {
        if (publicKeyBase64 == null || publicKeyBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("RSA 公钥不能为空");
        }
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64.trim());
            return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 公钥解析失败", e);
        }
    }

    private static PrivateKey toPrivateKey(String privateKeyBase64) {
        if (privateKeyBase64 == null || privateKeyBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("RSA 私钥不能为空");
        }
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64.trim());
            return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 私钥解析失败", e);
        }
    }

    private static String toPem(String type, String base64Key) {
        if (base64Key == null || base64Key.trim().isEmpty()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        String normalized = base64Key.replaceAll("\\s+", "");
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(type).append("-----\n");
        int index = 0;
        while (index < normalized.length()) {
            int end = Math.min(index + 64, normalized.length());
            sb.append(normalized, index, end).append('\n');
            index = end;
        }
        sb.append("-----END ").append(type).append("-----");
        return sb.toString();
    }

    private static void checkBytes(byte[] bytes, String name) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException(name + "不能为空");
        }
    }
}
