# RequestParamUtil

HTTP 请求参数工具类，同时支持 **GET** 与 **POST**，并兼顾过滤器中获取入参（含 `@PathVariable`）。

**包名：** `com.utility.RequestParamUtil`

---

## 功能简介

| 场景 | 如何取参 |
| --- | --- |
| GET QueryString | `getParam` / `getParamMap` / `getRequestInputs` |
| POST 表单 | 同上 |
| POST JSON / 原始报文 | `getBody` / `getBodyQuietly`；汇总时放入 `_body` |
| `@PathVariable` | `getPathVariables` / `getRequestInputs(request, pathPattern)` |

> 入参**可有可无**：汇总类方法在无参数时返回空 Map，不抛异常。

---

## 依赖

```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
```

---

## 方法一览

### 单参数

| 方法 | 说明 |
| --- | --- |
| `getParam(request, name)` | 单个参数，不存在返回 `null` |
| `getParam(request, name, defaultValue)` | 不存在或空白时返回默认值 |
| `getRequiredParam(request, name)` | 必填，缺失抛异常 |
| `getParams(request, name)` | 同名多值 |
| `getIntParam` / `getLongParam` / `getBoolParam` | 类型化取值 |

### 过滤器推荐：汇总入参

| 方法 | 说明 |
| --- | --- |
| `getRequestInputs(request)` | 汇总 PathVariable（Spring 属性）+ Query/Form + Body |
| `getRequestInputs(request, pathPattern)` | 同上，并按路径模板解析 PathVariable |
| `getPathVariables(request, pathPattern)` | 只取路径变量 |
| `getSpringPathVariables(request)` | 只取 Spring 已解析的 PathVariable |
| `extractPathVariables(path, pathPattern)` | 纯字符串路径模板解析 |
| `getBodyQuietly(request)` | 安静读 body，失败返回 `""` |
| `getLookupPath(request)` | 去掉 contextPath 后的路径 |

### 其他

| 方法 | 说明 |
| --- | --- |
| `getParamMap` / `getParamMapMulti` | 全部 Query/Form 参数 |
| `getBody` | 读请求体（失败抛异常） |
| `isGet` / `isPost` / `isJsonRequest` | 请求方法 / 类型判断 |

---

## 过滤器场景说明

### 为何 PathVariable 要特殊处理？

`@PathVariable` 是 Spring MVC 概念。普通 **Filter 在 DispatcherServlet 之前**执行时，Spring 往往还没写入路径变量，因此：

1. **推荐**：调用时传入与 Controller 一致的路径模板  
   `getRequestInputs(request, "/api/user/{userId}/order/{orderId}")`
2. **若在 HandlerInterceptor / 映射之后**：可直接 `getRequestInputs(request)`，会尝试读取 Spring 属性

### 汇总规则

1. 先放 PathVariable  
2. 再放 Query / Form（**同名时覆盖** PathVariable）  
3. 若读到非空 body，放入 key `_body`（常量 `RequestParamUtil.KEY_BODY`）

---

## 使用示例

### 1. 过滤器中获取全部入参（推荐）

```java
// Controller: @GetMapping("/api/user/{userId}/order/{orderId}")
// 实际请求: GET /api/user/1001/order/A200?from=app

Map<String, String> inputs = RequestParamUtil.getRequestInputs(
        request, "/api/user/{userId}/order/{orderId}");

// inputs =>
// userId=1001
// orderId=A200
// from=app
```

```java
public class ApiLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        // 入参可有可无；GET/POST 均可；无参数时为空 Map
        Map<String, String> inputs = RequestParamUtil.getRequestInputs(
                request, "/api/user/{userId}");

        // 打日志、验签等...
        chain.doFilter(req, resp);
    }
}
```

### 2. POST JSON + PathVariable

```text
POST /api/user/1001/save?source=web
Content-Type: application/json

{"name":"Tom"}
```

```java
Map<String, String> inputs = RequestParamUtil.getRequestInputs(
        request, "/api/user/{userId}/save");

String userId = inputs.get("userId");           // 1001
String source = inputs.get("source");           // web
String body = inputs.get(RequestParamUtil.KEY_BODY); // {"name":"Tom"}
```

### 3. 仅解析路径变量

```java
Map<String, String> pathVars = RequestParamUtil.getPathVariables(
        request, "/order/{orderId}");
String orderId = pathVars.get("orderId"); // 没有则为 null
```

### 4. GET / POST 表单单参

```java
String id = RequestParamUtil.getParam(request, "id");
int page = RequestParamUtil.getIntParam(request, "page", 1);
```

---

## 注意事项

- 汇总类方法**不强制**参数存在；没有入参就返回空 Map  
- Filter 里读 body 会消耗流，可能导致 Controller 读不到；需要的话请用 `ContentCachingRequestWrapper` 等可重复读包装  
- `pathPattern` 段数须与实际路径一致，例如模板 `/a/{id}` 无法匹配 `/a/1/b`  
- 不依赖 Spring 编译；读取 Spring PathVariable 通过属性名字符串完成
