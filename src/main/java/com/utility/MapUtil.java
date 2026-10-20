package com.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map 工具类（JDK 8）
 */
public final class MapUtil {

    private MapUtil() {
    }

    /**
     * 是否为 null 或空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 是否非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 获取值；map 或 key 不存在时返回默认值
     */
    @SuppressWarnings("unchecked")
    public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
        if (map == null || key == null || !map.containsKey(key)) {
            return defaultValue;
        }
        V value = map.get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取字符串值
     */
    public static String getStr(Map<?, ?> map, Object key) {
        if (isEmpty(map) || key == null || !map.containsKey(key)) {
            return null;
        }
        Object value = map.get(key);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * 获取字符串值，空白时返回默认值
     */
    public static String getStr(Map<?, ?> map, Object key, String defaultValue) {
        return StringUtil.defaultIfBlank(getStr(map, key), defaultValue);
    }

    /**
     * 新建 HashMap，并放入一对键值
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<K, V>(4);
        map.put(key, value);
        return map;
    }

    /**
     * 新建 HashMap，并放入两对键值
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<K, V>(8);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    /**
     * 新建 HashMap，并放入三对键值
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<K, V>(8);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    /**
     * 浅拷贝；null 返回空 Map
     */
    public static <K, V> Map<K, V> copy(Map<K, V> map) {
        if (isEmpty(map)) {
            return new HashMap<K, V>();
        }
        return new HashMap<K, V>(map);
    }

    /**
     * null 转空 Map
     */
    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return map == null ? Collections.<K, V>emptyMap() : map;
    }
}
