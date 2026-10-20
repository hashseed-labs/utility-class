package com.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类（JDK 8，基于 jjwt）
 * <p>
 * 使用 HS256 签名。密钥长度建议不少于 32 字节（256 bit）。
 */
public final class JwtUtil {

    private JwtUtil() {
    }

    /**
     * 生成 JWT
     *
     * @param subject      主题，通常放用户标识
     * @param secret       签名密钥（至少 32 字节）
     * @param expireMillis 过期时长（毫秒）
     * @return token 字符串
     */
    public static String createToken(String subject, String secret, long expireMillis) {
        return createToken(subject, Collections.<String, Object>emptyMap(), secret, expireMillis);
    }

    /**
     * 生成带自定义声明的 JWT
     *
     * @param subject      主题，通常放用户标识
     * @param claims       自定义声明，可为 null
     * @param secret       签名密钥（至少 32 字节）
     * @param expireMillis 过期时长（毫秒）
     * @return token 字符串
     */
    public static String createToken(String subject, Map<String, Object> claims,
                                     String secret, long expireMillis) {
        checkSecret(secret);
        if (expireMillis <= 0) {
            throw new IllegalArgumentException("过期时长必须大于 0");
        }

        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expireMillis);

        Map<String, Object> payload = new HashMap<String, Object>();
        if (claims != null && !claims.isEmpty()) {
            payload.putAll(claims);
        }

        return Jwts.builder()
                .setClaims(payload)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(toKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT，返回全部声明
     *
     * @throws IllegalArgumentException token 非法、签名错误或已过期
     */
    public static Claims parseClaims(String token, String secret) {
        checkToken(token);
        checkSecret(secret);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(toKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT 已过期", e);
        } catch (JwtException e) {
            throw new IllegalArgumentException("JWT 解析失败: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("JWT 解析失败", e);
        }
    }

    /**
     * 获取 subject
     */
    public static String getSubject(String token, String secret) {
        return parseClaims(token, secret).getSubject();
    }

    /**
     * 获取指定声明
     */
    public static Object getClaim(String token, String secret, String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("声明名不能为空");
        }
        return parseClaims(token, secret).get(key);
    }

    /**
     * 获取指定声明并转为字符串
     */
    public static String getClaimAsString(String token, String secret, String key) {
        Object value = getClaim(token, secret, key);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * 校验 token 是否有效（签名正确且未过期）
     */
    public static boolean isValid(String token, String secret) {
        try {
            parseClaims(token, secret);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 判断 token 是否已过期
     * <p>
     * 仅当能解析出过期时间且已过期时返回 true；签名错误等其他异常直接抛出。
     */
    public static boolean isExpired(String token, String secret) {
        checkToken(token);
        checkSecret(secret);
        try {
            parseClaims(token, secret);
            return false;
        } catch (IllegalArgumentException e) {
            if (e.getCause() instanceof ExpiredJwtException) {
                return true;
            }
            throw e;
        }
    }

    /**
     * 获取过期时间
     */
    public static Date getExpiration(String token, String secret) {
        return parseClaims(token, secret).getExpiration();
    }

    private static SecretKey toKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT 密钥长度不能少于 32 字节");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static void checkToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT token 不能为空");
        }
    }

    private static void checkSecret(String secret) {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT 密钥不能为空");
        }
    }
}
