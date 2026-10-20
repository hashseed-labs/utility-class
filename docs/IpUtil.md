# IpUtil

IP 工具类。

**包名：** `com.utility.IpUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `getClientIp(request)` | 获取客户端真实 IP（兼容代理头） |
| `isInternalIp(ip)` | 是否内网 IP |
| `getLocalIp()` | 本机 IP |

```java
String ip = IpUtil.getClientIp(request);
boolean lan = IpUtil.isInternalIp("192.168.1.1"); // true
```

依赖 `javax.servlet-api`（与 `RequestParamUtil` 相同）。
