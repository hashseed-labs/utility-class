package com.utility;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * 字符串处理工具类（JDK 8）
 * <p>
 * 提供判空、裁剪、拼接、截取、替换、大小写、命名转换等常用能力。
 */
public final class StringUtil {

    public static final String EMPTY = "";

    private StringUtil() {
    }

    // -------------------- 判空 --------------------

    /**
     * 是否为 null 或长度为 0
     */
    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    /**
     * 是否不为 null 且长度大于 0
     */
    public static boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    /**
     * 是否为 null、空串或仅空白字符
     */
    public static boolean isBlank(CharSequence text) {
        if (text == null || text.length() == 0) {
            return true;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含非空白字符
     */
    public static boolean isNotBlank(CharSequence text) {
        return !isBlank(text);
    }

    // -------------------- 默认值 / 裁剪 --------------------

    /**
     * null 转空串
     */
    public static String nullToEmpty(String text) {
        return text == null ? EMPTY : text;
    }

    /**
     * 空串转 null
     */
    public static String emptyToNull(String text) {
        return isEmpty(text) ? null : text;
    }

    /**
     * 空白转默认值
     */
    public static String defaultIfBlank(String text, String defaultValue) {
        return isBlank(text) ? defaultValue : text;
    }

    /**
     * 空串（含 null）转默认值
     */
    public static String defaultIfEmpty(String text, String defaultValue) {
        return isEmpty(text) ? defaultValue : text;
    }

    /**
     * trim；null 返回 null
     */
    public static String trim(String text) {
        return text == null ? null : text.trim();
    }

    /**
     * trim 后若为空则返回 null
     */
    public static String trimToNull(String text) {
        String trimmed = trim(text);
        return isEmpty(trimmed) ? null : trimmed;
    }

    /**
     * trim 后若为 null 则返回空串
     */
    public static String trimToEmpty(String text) {
        return text == null ? EMPTY : text.trim();
    }

    // -------------------- 相等 / 包含 --------------------

    /**
     * 空安全 equals
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.toString().equals(b.toString());
    }

    /**
     * 空安全 equalsIgnoreCase
     */
    public static boolean equalsIgnoreCase(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.toString().equalsIgnoreCase(b.toString());
    }

    /**
     * 是否包含子串（任一方为 null 返回 false）
     */
    public static boolean contains(CharSequence text, CharSequence search) {
        if (text == null || search == null) {
            return false;
        }
        return text.toString().contains(search);
    }

    /**
     * 是否以指定前缀开头
     */
    public static boolean startsWith(CharSequence text, CharSequence prefix) {
        if (text == null || prefix == null) {
            return false;
        }
        return text.toString().startsWith(prefix.toString());
    }

    /**
     * 是否以指定后缀结尾
     */
    public static boolean endsWith(CharSequence text, CharSequence suffix) {
        if (text == null || suffix == null) {
            return false;
        }
        return text.toString().endsWith(suffix.toString());
    }

    // -------------------- 截取 / 补齐 / 重复 --------------------

    /**
     * 安全截取；越界时返回能取到的部分，null 返回 null
     */
    public static String substring(String text, int start, int end) {
        if (text == null) {
            return null;
        }
        int length = text.length();
        if (start < 0) {
            start = 0;
        }
        if (end > length) {
            end = length;
        }
        if (start >= end) {
            return EMPTY;
        }
        return text.substring(start, end);
    }

    /**
     * 左侧截取 n 个字符
     */
    public static String left(String text, int len) {
        if (text == null) {
            return null;
        }
        if (len <= 0) {
            return EMPTY;
        }
        return text.length() <= len ? text : text.substring(0, len);
    }

    /**
     * 右侧截取 n 个字符
     */
    public static String right(String text, int len) {
        if (text == null) {
            return null;
        }
        if (len <= 0) {
            return EMPTY;
        }
        return text.length() <= len ? text : text.substring(text.length() - len);
    }

    /**
     * 超过最大长度时截断并追加省略号（默认 "..."）
     */
    public static String abbreviate(String text, int maxWidth) {
        return abbreviate(text, maxWidth, "...");
    }

    /**
     * 超过最大长度时截断并追加指定后缀
     */
    public static String abbreviate(String text, int maxWidth, String suffix) {
        if (text == null) {
            return null;
        }
        if (maxWidth <= 0) {
            return EMPTY;
        }
        String end = suffix == null ? EMPTY : suffix;
        if (text.length() <= maxWidth) {
            return text;
        }
        if (maxWidth <= end.length()) {
            return left(end, maxWidth);
        }
        return text.substring(0, maxWidth - end.length()) + end;
    }

    /**
     * 左侧补齐
     */
    public static String padLeft(String text, int size, char padChar) {
        String value = nullToEmpty(text);
        if (value.length() >= size) {
            return value;
        }
        StringBuilder sb = new StringBuilder(size);
        for (int i = value.length(); i < size; i++) {
            sb.append(padChar);
        }
        sb.append(value);
        return sb.toString();
    }

    /**
     * 右侧补齐
     */
    public static String padRight(String text, int size, char padChar) {
        String value = nullToEmpty(text);
        if (value.length() >= size) {
            return value;
        }
        StringBuilder sb = new StringBuilder(size);
        sb.append(value);
        for (int i = value.length(); i < size; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 重复字符串
     */
    public static String repeat(String text, int times) {
        if (text == null || times <= 0) {
            return EMPTY;
        }
        if (times == 1) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text.length() * times);
        for (int i = 0; i < times; i++) {
            sb.append(text);
        }
        return sb.toString();
    }

    /**
     * 反转字符串
     */
    public static String reverse(String text) {
        if (text == null) {
            return null;
        }
        return new StringBuilder(text).reverse().toString();
    }

    // -------------------- 替换 / 移除 --------------------

    /**
     * 替换全部；任一方为 null 时原样返回 text
     */
    public static String replace(String text, String search, String replacement) {
        if (isEmpty(text) || isEmpty(search) || replacement == null) {
            return text;
        }
        return text.replace(search, replacement);
    }

    /**
     * 移除所有空白字符
     */
    public static String removeWhitespace(String text) {
        if (isEmpty(text)) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!Character.isWhitespace(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 移除指定子串
     */
    public static String remove(String text, String remove) {
        return replace(text, remove, EMPTY);
    }

    // -------------------- 大小写 / 命名转换 --------------------

    /**
     * 首字母大写
     */
    public static String capitalize(String text) {
        if (isEmpty(text)) {
            return text;
        }
        char first = text.charAt(0);
        if (Character.isUpperCase(first)) {
            return text;
        }
        return Character.toUpperCase(first) + text.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String text) {
        if (isEmpty(text)) {
            return text;
        }
        char first = text.charAt(0);
        if (Character.isLowerCase(first)) {
            return text;
        }
        return Character.toLowerCase(first) + text.substring(1);
    }

    /**
     * 下划线转驼峰：user_name → userName；USER_NAME → userName
     */
    public static String underlineToCamel(String text) {
        if (isBlank(text)) {
            return text;
        }
        String value = text.toLowerCase();
        StringBuilder sb = new StringBuilder(value.length());
        boolean upperNext = false;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == '_') {
                upperNext = true;
                continue;
            }
            if (upperNext) {
                sb.append(Character.toUpperCase(ch));
                upperNext = false;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线：userName → user_name
     */
    public static String camelToUnderline(String text) {
        if (isBlank(text)) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text.length() + 8);
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    // -------------------- 拼接 / 分割 --------------------

    /**
     * 使用分隔符拼接（自动跳过 null）
     */
    public static String join(CharSequence delimiter, Object... elements) {
        if (elements == null || elements.length == 0) {
            return EMPTY;
        }
        String sep = delimiter == null ? EMPTY : delimiter.toString();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object element : elements) {
            if (element == null) {
                continue;
            }
            if (!first) {
                sb.append(sep);
            }
            sb.append(element);
            first = false;
        }
        return sb.toString();
    }

    /**
     * 使用分隔符拼接集合（自动跳过 null）
     */
    public static String join(CharSequence delimiter, Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return EMPTY;
        }
        String sep = delimiter == null ? EMPTY : delimiter.toString();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object element : collection) {
            if (element == null) {
                continue;
            }
            if (!first) {
                sb.append(sep);
            }
            sb.append(element);
            first = false;
        }
        return sb.toString();
    }

    /**
     * 按分隔符分割并去掉空白项
     */
    public static List<String> splitTrim(String text, String separator) {
        List<String> result = new ArrayList<String>();
        if (isEmpty(text)) {
            return result;
        }
        String sep = separator == null ? EMPTY : separator;
        if (isEmpty(sep)) {
            result.add(trimToEmpty(text));
            return result;
        }
        String[] parts = text.split(java.util.regex.Pattern.quote(sep), -1);
        for (String part : parts) {
            String item = trimToEmpty(part);
            if (isNotEmpty(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // -------------------- 判断 / 其他 --------------------

    /**
     * 是否全为数字（不含符号、小数点）
     */
    public static boolean isNumeric(CharSequence text) {
        if (isEmpty(text)) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字节长度（默认 UTF-8）
     */
    public static int byteLength(String text) {
        return byteLength(text, StandardCharsets.UTF_8);
    }

    /**
     * 字节长度
     */
    public static int byteLength(String text, Charset charset) {
        if (text == null) {
            return 0;
        }
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        return text.getBytes(cs).length;
    }

    /**
     * 生成不带横线的 UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", EMPTY);
    }

    /**
     * 生成标准 UUID
     */
    public static String uuidStandard() {
        return UUID.randomUUID().toString();
    }

    /**
     * 隐藏中间字符，保留左右各保留位数（常用于手机号、身份证脱敏）
     * <p>
     * 例如：{@code hide("13812345678", 3, 4)} → {@code 138****5678}
     */
    public static String hide(String text, int prefixLen, int suffixLen) {
        return hide(text, prefixLen, suffixLen, '*');
    }

    /**
     * 隐藏中间字符
     */
    public static String hide(String text, int prefixLen, int suffixLen, char maskChar) {
        if (isEmpty(text)) {
            return text;
        }
        if (prefixLen < 0) {
            prefixLen = 0;
        }
        if (suffixLen < 0) {
            suffixLen = 0;
        }
        int length = text.length();
        if (prefixLen + suffixLen >= length) {
            return text;
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(text, 0, prefixLen);
        for (int i = 0; i < length - prefixLen - suffixLen; i++) {
            sb.append(maskChar);
        }
        sb.append(text, length - suffixLen, length);
        return sb.toString();
    }

    /**
     * 格式化：将 {} 依次替换为参数（忽略多余参数；参数不足时保留 {}）
     * <p>
     * 例如：{@code format("你好，{}，今天是{}", "张三", "周一")}
     */
    public static String format(String template, Object... args) {
        if (isEmpty(template) || args == null || args.length == 0) {
            return template;
        }
        StringBuilder sb = new StringBuilder(template.length() + 16);
        int argIndex = 0;
        for (int i = 0; i < template.length(); i++) {
            char ch = template.charAt(i);
            if (ch == '{' && i + 1 < template.length() && template.charAt(i + 1) == '}') {
                if (argIndex < args.length) {
                    sb.append(args[argIndex] == null ? "null" : args[argIndex]);
                    argIndex++;
                } else {
                    sb.append("{}");
                }
                i++;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
