package com.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类（JDK 8）
 * <p>
 * 内置常见校验：手机号、邮箱、身份证、URL、IP 等。
 */
public final class RegexUtil {

    /** 中国大陆手机号 */
    public static final String REGEX_MOBILE = "^1[3-9]\\d{9}$";

    /** 邮箱 */
    public static final String REGEX_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /** 18 位身份证（格式校验，不含强校验位算法） */
    public static final String REGEX_ID_CARD =
            "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$";

    /** URL */
    public static final String REGEX_URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    /** IPv4 */
    public static final String REGEX_IPV4 =
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    /** 整数（可含正负号） */
    public static final String REGEX_INTEGER = "^[+-]?\\d+$";

    private RegexUtil() {
    }

    /**
     * 是否全匹配
     */
    public static boolean isMatch(String regex, CharSequence text) {
        if (regex == null || text == null) {
            return false;
        }
        return Pattern.matches(regex, text);
    }

    /**
     * 是否包含匹配
     */
    public static boolean contains(String regex, CharSequence text) {
        if (regex == null || text == null) {
            return false;
        }
        return Pattern.compile(regex).matcher(text).find();
    }

    /**
     * 提取全部匹配内容
     */
    public static List<String> findAll(String regex, CharSequence text) {
        List<String> result = new ArrayList<String>();
        if (regex == null || text == null) {
            return result;
        }
        Matcher matcher = Pattern.compile(regex).matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 替换全部匹配
     */
    public static String replaceAll(CharSequence text, String regex, String replacement) {
        if (text == null) {
            return null;
        }
        if (regex == null) {
            return text.toString();
        }
        return Pattern.compile(regex).matcher(text).replaceAll(replacement == null ? "" : replacement);
    }

    public static boolean isMobile(CharSequence text) {
        return isMatch(REGEX_MOBILE, text);
    }

    public static boolean isEmail(CharSequence text) {
        return isMatch(REGEX_EMAIL, text);
    }

    public static boolean isIdCard(CharSequence text) {
        return isMatch(REGEX_ID_CARD, text);
    }

    public static boolean isUrl(CharSequence text) {
        return isMatch(REGEX_URL, text);
    }

    public static boolean isIpv4(CharSequence text) {
        return isMatch(REGEX_IPV4, text);
    }

    public static boolean isInteger(CharSequence text) {
        return isMatch(REGEX_INTEGER, text);
    }
}
