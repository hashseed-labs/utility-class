package com.utility;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对象工具类（JDK 8）
 */
public final class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * 是否为 null
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 是否非 null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 全部为 null
     */
    public static boolean isAllNull(Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        }
        for (Object obj : objects) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否存在 null
     */
    public static boolean hasNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 空安全 equals
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * 取第一个非 null 值
     */
    @SafeVarargs
    public static <T> T firstNonNull(T... values) {
        if (values == null) {
            return null;
        }
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * null 时返回默认值
     */
    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * 空安全 toString；null 返回 "null"
     */
    public static String toString(Object obj) {
        return String.valueOf(obj);
    }

    /**
     * 空安全 toString；null 返回默认值
     */
    public static String toString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : obj.toString();
    }

    /**
     * hashCode（空安全）
     */
    public static int hashCode(Object... values) {
        return Arrays.hashCode(values);
    }
}
