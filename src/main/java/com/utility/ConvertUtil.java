package com.utility;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类型转换工具类（JDK 8）
 * <p>
 * 支持常用类型之间的转换；源值为空或转换失败时抛出 {@link IllegalArgumentException}。
 */
public final class ConvertUtil {

    private ConvertUtil() {
    }

    // -------------------- String --------------------

    /**
     * 任意对象转字符串
     *
     * @throws IllegalArgumentException value 为 null
     */
    public static String toStr(Object value) {
        if (value == null) {
            throw fail(value, "String");
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    // -------------------- Integer / int --------------------

    /**
     * 转 Integer
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static Integer toInt(Object value) {
        if (value == null) {
            throw fail(value, "Integer");
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        String text = requireText(value, "Integer");
        try {
            if (text.contains(".")) {
                return new BigDecimal(text).intValue();
            }
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            throw fail(value, "Integer", e);
        }
    }

    /**
     * 转 int
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static int toIntValue(Object value) {
        return toInt(value);
    }

    // -------------------- Long / long --------------------

    /**
     * 转 Long
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static Long toLong(Object value) {
        if (value == null) {
            throw fail(value, "Long");
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        String text = requireText(value, "Long");
        try {
            if (text.contains(".")) {
                return new BigDecimal(text).longValue();
            }
            return Long.valueOf(text);
        } catch (NumberFormatException e) {
            throw fail(value, "Long", e);
        }
    }

    /**
     * 转 long
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static long toLongValue(Object value) {
        return toLong(value);
    }

    // -------------------- Double / double --------------------

    /**
     * 转 Double
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static Double toDouble(Object value) {
        if (value == null) {
            throw fail(value, "Double");
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        String text = requireText(value, "Double");
        try {
            return Double.valueOf(text);
        } catch (NumberFormatException e) {
            throw fail(value, "Double", e);
        }
    }

    /**
     * 转 double
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static double toDoubleValue(Object value) {
        return toDouble(value);
    }

    // -------------------- Float / float --------------------

    /**
     * 转 Float
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static Float toFloat(Object value) {
        if (value == null) {
            throw fail(value, "Float");
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        String text = requireText(value, "Float");
        try {
            return Float.valueOf(text);
        } catch (NumberFormatException e) {
            throw fail(value, "Float", e);
        }
    }

    /**
     * 转 float
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static float toFloatValue(Object value) {
        return toFloat(value);
    }

    // -------------------- Boolean / boolean --------------------

    /**
     * 转 Boolean
     * <p>
     * 支持：true/false、1/0、yes/no、y/n、是/否（忽略大小写）
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static Boolean toBool(Object value) {
        if (value == null) {
            throw fail(value, "Boolean");
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        String text = requireText(value, "Boolean").toLowerCase();
        if ("true".equals(text) || "1".equals(text) || "yes".equals(text)
                || "y".equals(text) || "是".equals(text)) {
            return Boolean.TRUE;
        }
        if ("false".equals(text) || "0".equals(text) || "no".equals(text)
                || "n".equals(text) || "否".equals(text)) {
            return Boolean.FALSE;
        }
        throw fail(value, "Boolean");
    }

    /**
     * 转 boolean
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static boolean toBoolValue(Object value) {
        return toBool(value);
    }

    // -------------------- BigDecimal --------------------

    /**
     * 转 BigDecimal
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            throw fail(value, "BigDecimal");
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Integer || value instanceof Long
                || value instanceof Short || value instanceof Byte) {
            return BigDecimal.valueOf(((Number) value).longValue());
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        String text = requireText(value, "BigDecimal");
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            throw fail(value, "BigDecimal", e);
        }
    }

    // -------------------- Date --------------------

    /**
     * 转 Date；支持 Date、毫秒时间戳（Number）、以及常用时间格式 / 时间戳字符串
     * <p>
     * 常用格式：yyyy-MM-dd HH:mm:ss、yyyy-MM-dd、yyyyMMddHHmmss、yyyyMMdd；
     * 10/13 位纯数字按秒/毫秒时间戳处理
     *
     * @throws IllegalArgumentException 为空或无法转换
     */
    public static Date toDate(Object value) {
        if (value == null) {
            throw fail(value, "Date");
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        String text = requireText(value, "Date");

        String pattern = guessDatePattern(text);
        if (pattern != null) {
            try {
                return DateUtil.parseToDate(text, pattern);
            } catch (IllegalArgumentException e) {
                throw fail(value, "Date", e);
            }
        }

        if (isDigits(text) && (text.length() == 10 || text.length() == 13)) {
            try {
                long epoch = Long.parseLong(text);
                if (text.length() == 10) {
                    epoch = epoch * 1000L;
                }
                return new Date(epoch);
            } catch (NumberFormatException e) {
                throw fail(value, "Date", e);
            }
        }
        throw fail(value, "Date");
    }

    // -------------------- 内部方法 --------------------

    private static String requireText(Object value, String targetType) {
        String text = value.toString().trim();
        if (text.isEmpty()) {
            throw fail(value, targetType);
        }
        return text;
    }

    private static boolean isDigits(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static String guessDatePattern(String text) {
        int len = text.length();
        if (len == 19 && text.charAt(4) == '-' && text.charAt(10) == ' ') {
            return DateUtil.PATTERN_DATETIME;
        }
        if (len == 10 && text.charAt(4) == '-') {
            return DateUtil.PATTERN_DATE;
        }
        if (len == 14 && isDigits(text)) {
            return DateUtil.PATTERN_DATETIME_COMPACT;
        }
        if (len == 8 && isDigits(text)) {
            return DateUtil.PATTERN_DATE_COMPACT;
        }
        return null;
    }

    private static IllegalArgumentException fail(Object value, String targetType) {
        return fail(value, targetType, null);
    }

    private static IllegalArgumentException fail(Object value, String targetType, Throwable cause) {
        String message = "类型转换失败, value=" + value + ", targetType=" + targetType;
        if (cause == null) {
            return new IllegalArgumentException(message);
        }
        return new IllegalArgumentException(message, cause);
    }
}
