package com.utility;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 加解密工具类（JDK 8）
 * <p>
 * 算法：AES/CBC/PKCS5Padding。<br>
 * 密文格式：Base64( IV[16字节] + 密文 )，便于与前端（如 CryptoJS）互通。<br>
 * 密钥长度：16 字节 = AES-128，24 字节 = AES-192，32 字节 = AES-256。
 */
public final class AesUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    private AesUtil() {
    }

    /**
     * 加密字符串，返回 Base64 密文
     *
     * @param plainText 明文
     * @param key      AES 密钥（16/24/32 字节）
     * @return Base64(IV + 密文)
     */
    public static String encrypt(String plainText, String key) {
        if (plainText == null) {
            throw new IllegalArgumentException("明文不能为空");
        }
        byte[] encrypted = encrypt(plainText.getBytes(StandardCharsets.UTF_8), toKeyBytes(key));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密 Base64 密文，返回明文字符串
     *
     * @param cipherText Base64(IV + 密文)
     * @param key        AES 密钥（16/24/32 字节）
     * @return 明文
     */
    public static String decrypt(String cipherText, String key) {
        if (cipherText == null || cipherText.trim().isEmpty()) {
            throw new IllegalArgumentException("密文不能为空");
        }
        byte[] decoded = Base64.getDecoder().decode(cipherText.trim());
        byte[] plain = decrypt(decoded, toKeyBytes(key));
        return new String(plain, StandardCharsets.UTF_8);
    }

    /**
     * 加密字节数组，返回 IV + 密文
     */
    public static byte[] encrypt(byte[] plainBytes, byte[] keyBytes) {
        checkPlain(plainBytes);
        checkKey(keyBytes);
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, ALGORITHM), new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(plainBytes);

            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("AES 加密失败", e);
        }
    }

    /**
     * 解密字节数组（前 16 字节为 IV）
     */
    public static byte[] decrypt(byte[] cipherBytes, byte[] keyBytes) {
        if (cipherBytes == null || cipherBytes.length <= IV_LENGTH) {
            throw new IllegalArgumentException("密文长度不合法");
        }
        checkKey(keyBytes);
        try {
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(cipherBytes, 0, iv, 0, IV_LENGTH);
            byte[] encrypted = new byte[cipherBytes.length - IV_LENGTH];
            System.arraycopy(cipherBytes, IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, ALGORITHM), new IvParameterSpec(iv));
            return cipher.doFinal(encrypted);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("AES 解密失败", e);
        }
    }

    /**
     * 生成随机 AES 密钥（Base64 编码）
     *
     * @param bitLength 128 / 192 / 256
     */
    public static String generateKey(int bitLength) {
        if (bitLength != 128 && bitLength != 192 && bitLength != 256) {
            throw new IllegalArgumentException("AES 密钥位数仅支持 128、192、256");
        }
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(bitLength, new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new IllegalArgumentException("生成 AES 密钥失败", e);
        }
    }

    /**
     * 生成 128 位 AES 密钥（Base64）
     */
    public static String generateKey() {
        return generateKey(128);
    }

    private static byte[] toKeyBytes(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("AES 密钥不能为空");
        }
        // 优先按 Base64 解码；失败则按 UTF-8 原始字节
        try {
            byte[] decoded = Base64.getDecoder().decode(key);
            if (decoded.length == 16 || decoded.length == 24 || decoded.length == 32) {
                return decoded;
            }
        } catch (IllegalArgumentException ignore) {
            // fall through
        }
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        checkKey(raw);
        return raw;
    }

    private static void checkKey(byte[] keyBytes) {
        if (keyBytes == null
                || (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32)) {
            throw new IllegalArgumentException("AES 密钥长度必须为 16、24 或 32 字节");
        }
    }

    private static void checkPlain(byte[] plainBytes) {
        if (plainBytes == null) {
            throw new IllegalArgumentException("明文不能为空");
        }
    }
}
