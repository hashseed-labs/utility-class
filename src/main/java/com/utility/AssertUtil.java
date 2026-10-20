package com.utility;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类（JDK 8）
 * <p>
 * 条件不满足时抛出 {@link IllegalArgumentException}。
 */
public final class AssertUtil {

    private AssertUtil() {
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(CharSequence text, String message) {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void equals(Object a, Object b, String message) {
        if (!ObjectUtil.equals(a, b)) {
            throw new IllegalArgumentException(message);
        }
    }
}
