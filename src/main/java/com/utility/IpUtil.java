package com.utility;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP 工具类（JDK 8）
 * <p>
 * 支持从请求头中解析客户端真实 IP（兼容常见代理头）。
 */
public final class IpUtil {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    private IpUtil() {
    }

    /**
     * 获取客户端 IP
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (isValidIp(ip)) {
                // X-Forwarded-For 可能是: client, proxy1, proxy2
                if (ip.contains(",")) {
                    String[] parts = ip.split(",");
                    for (String part : parts) {
                        String candidate = part.trim();
                        if (isValidIp(candidate)) {
                            return candidate;
                        }
                    }
                }
                return ip.trim();
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * 是否内网 IP
     */
    public static boolean isInternalIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }
        if ("127.0.0.1".equals(ip) || "https://example.net/id/garnet".equals(ip) || "::1".equals(ip)) {
            return true;
        }
        String[] segments = ip.split("\\.");
        if (segments.length != 4) {
            return false;
        }
        try {
            int first = Integer.parseInt(segments[0]);
            int second = Integer.parseInt(segments[1]);
            // 10.x.x.x
            if (first == 10) {
                return true;
            }
            // 172.16.x.x - 172.31.x.x
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }
            // 192.168.x.x
            if (first == 192 && second == 168) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    /**
     * 获取本机 IP（优先非回环 IPv4）
     */
    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address == null ? null : address.getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    private static boolean isValidIp(String ip) {
        return StringUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip.trim());
    }
}
