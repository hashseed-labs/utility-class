package com.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类（JDK 8，基于 Jackson）
 */
public final class JsonUtil {

    private static final ObjectMapper MAPPER = createMapper();

    private JsonUtil() {
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    /**
     * 获取全局 ObjectMapper（可按需继续配置，谨慎修改）
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * 对象转 JSON 字符串
     */
    public static String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("对象转 JSON 失败", e);
        }
    }

    /**
     * 对象转格式化 JSON
     */
    public static String toPrettyJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("对象转 JSON 失败", e);
        }
    }

    /**
     * JSON 转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtil.isBlank(json)) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("目标类型不能为空");
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 转对象失败", e);
        }
    }

    /**
     * JSON 转复杂泛型（如 List&lt;User&gt;）
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (StringUtil.isBlank(json)) {
            return null;
        }
        if (typeReference == null) {
            throw new IllegalArgumentException("TypeReference 不能为空");
        }
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 转对象失败", e);
        }
    }

    /**
     * JSON 转 List
     */
    public static <T> List<T> toList(String json, Class<T> elementClass) {
        if (StringUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        if (elementClass == null) {
            throw new IllegalArgumentException("元素类型不能为空");
        }
        try {
            JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass);
            List<T> list = MAPPER.readValue(json, type);
            return list == null ? Collections.<T>emptyList() : list;
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 转 List 失败", e);
        }
    }

    /**
     * JSON 转 Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) {
        if (StringUtil.isBlank(json)) {
            return Collections.emptyMap();
        }
        try {
            Map<String, Object> map = MAPPER.readValue(json, Map.class);
            return map == null ? Collections.<String, Object>emptyMap() : map;
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 转 Map 失败", e);
        }
    }

    /**
     * 对象深拷贝（通过 JSON 序列化）
     */
    public static <T> T deepCopy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return fromJson(toJson(source), targetClass);
    }

    /**
     * 是否为合法 JSON 对象或数组
     */
    public static boolean isJson(String text) {
        if (StringUtil.isBlank(text)) {
            return false;
        }
        String value = text.trim();
        try {
            MAPPER.readTree(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
