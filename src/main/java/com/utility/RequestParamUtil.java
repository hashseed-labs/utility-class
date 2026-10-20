package com.utility;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP 请求参数工具类（JDK 8）
 * <p>
 * 同时支持 GET 与 POST：
 * <ul>
 *   <li>GET：读取 QueryString 参数</li>
 *   <li>POST form（application/x-www-form-urlencoded / multipart）：读取表单参数</li>
 *   <li>POST JSON / 原始报文：读取请求体 {@link #getBody(HttpServletRequest)}</li>
 *   <li>PathVariable：通过路径模板解析，或读取 Spring 写入的 URI 模板变量</li>
 * </ul>
 * Servlet 规范下，{@code getParameter*} 会合并 QueryString 与表单参数。
 */
public final class RequestParamUtil {

    /**
     * Spring MVC 写入的 PathVariable 属性名。
     * <p>
     * 注意：普通 Filter 在 DispatcherServlet 之前执行时，该属性通常还不存在；
     * 在 HandlerInterceptor 或映射完成之后一般可读到。
     */
    public static final String SPRING_URI_TEMPLATE_VARIABLES =
            "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables";

    /** 汇总入参时，JSON/原始请求体放入该 key（仅当成功读到非空 body） */
    public static final String KEY_BODY = "_body";

    private RequestParamUtil() {
    }

    /**
     * 获取单个参数（GET / POST 表单均适用）
     *
     * @return 不存在时返回 null
     */
    public static String getParam(HttpServletRequest request, String name) {
        checkRequest(request);
        checkName(name);
        return request.getParameter(name);
    }

    /**
     * 获取单个参数，不存在或空白时返回默认值
     */
    public static String getParam(HttpServletRequest request, String name, String defaultValue) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取必填参数；不存在或空白时抛异常
     */
    public static String getRequiredParam(HttpServletRequest request, String name) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("缺少请求参数: " + name);
        }
        return value;
    }

    /**
     * 获取同名多值参数
     */
    public static String[] getParams(HttpServletRequest request, String name) {
        checkRequest(request);
        checkName(name);
        String[] values = request.getParameterValues(name);
        return values == null ? new String[0] : values;
    }

    /**
     * 获取 Integer 参数；不存在返回 null；存在但无法转换则抛异常
     */
    public static Integer getIntParam(HttpServletRequest request, String name) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return ConvertUtil.toInt(value.trim());
    }

    /**
     * 获取 int 参数；不存在或空白时返回默认值
     */
    public static int getIntParam(HttpServletRequest request, String name, int defaultValue) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return ConvertUtil.toIntValue(value.trim());
    }

    /**
     * 获取 Long 参数；不存在返回 null
     */
    public static Long getLongParam(HttpServletRequest request, String name) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return ConvertUtil.toLong(value.trim());
    }

    /**
     * 获取 long 参数；不存在或空白时返回默认值
     */
    public static long getLongParam(HttpServletRequest request, String name, long defaultValue) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return ConvertUtil.toLongValue(value.trim());
    }

    /**
     * 获取 Boolean 参数；不存在返回 null
     */
    public static Boolean getBoolParam(HttpServletRequest request, String name) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return ConvertUtil.toBool(value.trim());
    }

    /**
     * 获取 boolean 参数；不存在或空白时返回默认值
     */
    public static boolean getBoolParam(HttpServletRequest request, String name, boolean defaultValue) {
        String value = getParam(request, name);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return ConvertUtil.toBoolValue(value.trim());
    }

    /**
     * 获取全部参数（每个 key 只取第一个值）
     * <p>
     * 适用于 GET QueryString、POST 表单；不包含 JSON 请求体中的字段。
     * 无参数时返回空 Map（不会返回 null）。
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        checkRequest(request);
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new LinkedHashMap<String, String>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                result.put(entry.getKey(), values[0]);
            } else {
                result.put(entry.getKey(), null);
            }
        }
        return result;
    }

    /**
     * 获取全部参数（保留多值）
     */
    public static Map<String, String[]> getParamMapMulti(HttpServletRequest request) {
        checkRequest(request);
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<String, String[]>(parameterMap));
    }

    /**
     * 过滤器场景：汇总请求入参（入参可有可无，GET / POST 均适用）。
     * <p>
     * 汇总内容：
     * <ol>
     *   <li>PathVariable：优先读 Spring 的 URI 模板变量属性；没有则为空</li>
     *   <li>QueryString / 表单参数</li>
     *   <li>若能安静读到非空请求体，放入 key {@link #KEY_BODY}</li>
     * </ol>
     * 同名参数时：Query / Form 覆盖 PathVariable。<br>
     * 无任何入参时返回空 Map，不抛异常。
     */
    public static Map<String, String> getRequestInputs(HttpServletRequest request) {
        return getRequestInputs(request, null);
    }

    /**
     * 过滤器场景：汇总请求入参，并按路径模板解析 PathVariable。
     * <p>
     * 适用于 Filter 在 DispatcherServlet 之前、Spring 尚未写入 PathVariable 属性的情况。
     *
     * @param request     请求
     * @param pathPattern 路径模板，例如 {@code /api/user/{userId}/order/{orderId}}；
     *                    可为 null（此时仅尝试读取 Spring 属性中的 PathVariable）
     * @return 入参 Map，永不为 null
     */
    public static Map<String, String> getRequestInputs(HttpServletRequest request, String pathPattern) {
        checkRequest(request);
        Map<String, String> result = new LinkedHashMap<String, String>();

        // 1. PathVariable
        result.putAll(getPathVariables(request, pathPattern));

        // 2. Query / Form（覆盖同名 PathVariable）
        result.putAll(getParamMap(request));

        // 3. 请求体（可有可无；读失败不影响其他参数）
        String body = getBodyQuietly(request);
        if (body != null && !body.isEmpty()) {
            result.put(KEY_BODY, body);
        }
        return result;
    }

    /**
     * 仅获取 PathVariable。
     * <p>
     * 先读 Spring 属性；若传入 pathPattern，再按模板从 URI 解析并合并（模板解析结果覆盖同名 Spring 值）。
     * 解析不到时返回空 Map，不抛异常。
     *
     * @param pathPattern 例如 {@code /user/{id}}，可为 null
     */
    public static Map<String, String> getPathVariables(HttpServletRequest request, String pathPattern) {
        checkRequest(request);
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.putAll(getSpringPathVariables(request));
        if (pathPattern != null && !pathPattern.trim().isEmpty()) {
            result.putAll(extractPathVariables(getLookupPath(request), pathPattern.trim()));
        }
        return result;
    }

    /**
     * 仅获取 Spring 已解析的 PathVariable（可能为空）
     */
    public static Map<String, String> getSpringPathVariables(HttpServletRequest request) {
        checkRequest(request);
        Object attr = request.getAttribute(SPRING_URI_TEMPLATE_VARIABLES);
        if (!(attr instanceof Map)) {
            return Collections.emptyMap();
        }
        Map<?, ?> raw = (Map<?, ?>) attr;
        if (raw.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            result.put(String.valueOf(entry.getKey()),
                    entry.getValue() == null ? null : String.valueOf(entry.getValue()));
        }
        return result;
    }

    /**
     * 按路径模板解析 PathVariable。
     *
     * @param path        实际路径，如 {@code /api/user/1001}
     * @param pathPattern 模板，如 {@code /api/user/{userId}}
     * @return 解析结果；不匹配时返回空 Map
     */
    public static Map<String, String> extractPathVariables(String path, String pathPattern) {
        if (path == null || pathPattern == null) {
            return Collections.emptyMap();
        }
        String normalizedPath = normalizePath(path);
        String normalizedPattern = normalizePath(pathPattern);
        String[] pathSegments = splitPath(normalizedPath);
        String[] patternSegments = splitPath(normalizedPattern);
        if (pathSegments.length != patternSegments.length) {
            return Collections.emptyMap();
        }

        Map<String, String> result = new LinkedHashMap<String, String>();
        for (int i = 0; i < patternSegments.length; i++) {
            String patternSeg = patternSegments[i];
            String pathSeg = pathSegments[i];
            if (isPathVariable(patternSeg)) {
                String name = patternSeg.substring(1, patternSeg.length() - 1).trim();
                if (!name.isEmpty()) {
                    result.put(name, urlDecode(pathSeg));
                }
            } else if (!patternSeg.equals(pathSeg)) {
                return Collections.emptyMap();
            }
        }
        return result;
    }

    /**
     * 读取请求体原文（适用于 POST JSON / XML / 明文等）
     * <p>
     * 注意：请求体通常只能读取一次，读取后框架或其他代码可能无法再次读取。
     */
    public static String getBody(HttpServletRequest request) {
        checkRequest(request);
        try {
            return readBody(request);
        } catch (IOException e) {
            throw new IllegalArgumentException("读取请求体失败", e);
        }
    }

    /**
     * 安静读取请求体：失败或无内容时返回空字符串，不抛异常。
     * <p>
     * 适合过滤器中“有则带上、没有也无妨”的场景。
     * 若需在过滤器读取 body 后仍让 Controller 读到，请使用可重复读 Request 包装。
     */
    public static String getBodyQuietly(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        try {
            String body = readBody(request);
            return body == null ? "" : body;
        } catch (Exception e) {
            return "";
        }
    }

    private static String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        if (reader == null) {
            return "";
        }
        char[] buffer = new char[1024];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, len);
        }
        return sb.toString();
    }

    /**
     * 判断是否为 GET
     */
    public static boolean isGet(HttpServletRequest request) {
        checkRequest(request);
        return "GET".equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断是否为 POST
     */
    public static boolean isPost(HttpServletRequest request) {
        checkRequest(request);
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    /**
     * 获取 Content-Type（可能为 null）
     */
    public static String getContentType(HttpServletRequest request) {
        checkRequest(request);
        return request.getContentType();
    }

    /**
     * 是否为 JSON 请求体
     */
    public static boolean isJsonRequest(HttpServletRequest request) {
        String contentType = getContentType(request);
        return contentType != null && contentType.toLowerCase().contains("application/json");
    }

    /**
     * 获取用于匹配的路径（去掉 contextPath）
     */
    public static String getLookupPath(HttpServletRequest request) {
        checkRequest(request);
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (uri == null) {
            return "";
        }
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        if (uri.isEmpty()) {
            return "/";
        }
        return uri;
    }

    private static String normalizePath(String path) {
        String value = path.trim();
        if (value.isEmpty()) {
            return "/";
        }
        if (!value.startsWith("/")) {
            value = "/" + value;
        }
        if (value.length() > 1 && value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private static String[] splitPath(String path) {
        String value = normalizePath(path);
        if ("/".equals(value)) {
            return new String[0];
        }
        // 去掉开头的 /
        return value.substring(1).split("/");
    }

    private static boolean isPathVariable(String segment) {
        return segment != null
                && segment.length() >= 3
                && segment.charAt(0) == '{'
                && segment.charAt(segment.length() - 1) == '}';
    }

    private static String urlDecode(String value) {
        if (value == null) {
            return null;
        }
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private static void checkRequest(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest 不能为空");
        }
    }

    private static void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("参数名不能为空");
        }
    }
}
