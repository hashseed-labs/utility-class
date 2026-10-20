package com.utility;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 摘要 / 签名工具类（JDK 8）
 * <p>
 * 支持 MD5、SHA-1、SHA-256、HmacSHA256。
 */
public final class DigestUtil {

    private DigestUtil() {
    }

    public static String md5(String text) {
        return digestHex("MD5", text, StandardCharsets.UTF_8);
    }

    public static String md5(byte[] data) {
        return toHex(digest("MD5", data));
    }

    public static String sha1(String text) {
        return digestHex("SHA-1", text, StandardCharsets.UTF_8);
    }

    public static String sha256(String text) {
        return digestHex("SHA-256", text, StandardCharsets.UTF_8);
    }

    public static String sha256(byte[] data) {
        return toHex(digest("SHA-256", data));
    }

    /**
     * HmacSHA256，返回十六进制小写字符串
     */
    public static String hmacSha256(String text, String secret) {
        if (text == null || secret == null) {
            throw new IllegalArgumentException("文本或密钥不能为空");
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return toHex(mac.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalArgumentException("HmacSHA256 计算失败", e);
        }
    }

    public static String digestHex(String algorithm, String text, Charset charset) {
        if (text == null) {
            throw new IllegalArgumentException("摘要文本不能为空");
        }
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        return toHex(digest(algorithm, text.getBytes(cs)));
    }

    public static byte[] digest(String algorithm, byte[] data) {
        if (algorithm == null || algorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("摘要算法不能为空");
        }
        if (data == null) {
            throw new IllegalArgumentException("摘要数据不能为空");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return digest.digest(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("摘要计算失败: " + algorithm, e);
        }
    }

    /**
     * 字节数组转十六进制小写
     */
    public static String toHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
