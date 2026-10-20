package com.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数组工具类（JDK 8）
 */
public final class ArrayUtil {

    private ArrayUtil() {
    }

    /**
     * 是否为 null 或长度 0
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 是否非空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 长度（null 视为 0）
     */
    public static int length(Object[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * 是否包含元素（空安全 equals）
     */
    public static <T> boolean contains(T[] array, T element) {
        if (isEmpty(array)) {
            return false;
        }
        for (T item : array) {
            if (ObjectUtil.equals(item, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 下标获取；越界或 null 数组返回 null
     */
    public static <T> T get(T[] array, int index) {
        if (array == null || index < 0 || index >= array.length) {
            return null;
        }
        return array[index];
    }

    /**
     * 数组转 List（不可变视图之外的可变新列表）
     */
    public static <T> List<T> toList(T[] array) {
        if (isEmpty(array)) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(Arrays.asList(array));
    }

    /**
     * 拼接两个数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(T[] first, T[] second) {
        if (first == null && second == null) {
            return null;
        }
        if (isEmpty(first)) {
            return second == null ? first : second.clone();
        }
        if (isEmpty(second)) {
            return first.clone();
        }
        T[] result = (T[]) Array.newInstance(first.getClass().getComponentType(), first.length + second.length);
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 用分隔符拼接（跳过 null）
     */
    public static String join(Object[] array, CharSequence delimiter) {
        if (isEmpty(array)) {
            return StringUtil.EMPTY;
        }
        return StringUtil.join(delimiter, array);
    }

    /**
     * 反转数组（返回新数组，不修改原数组）
     */
    public static <T> T[] reverse(T[] array) {
        if (isEmpty(array)) {
            return array;
        }
        T[] copy = array.clone();
        for (int i = 0, j = copy.length - 1; i < j; i++, j--) {
            T tmp = copy[i];
            copy[i] = copy[j];
            copy[j] = tmp;
        }
        return copy;
    }

    /**
     * 去重（保留首次出现顺序）
     */
    public static <T> List<T> distinct(T[] array) {
        if (isEmpty(array)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<T>();
        for (T item : array) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
