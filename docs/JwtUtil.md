# JwtUtil

JWT 工具类（基于 jjwt，HS256）。

**包名：** `com.utility.JwtUtil`

---

## 功能简介

生成、解析、校验 JWT。使用 HS256 签名，密钥长度不少于 **32 字节**。

---

## 依赖

项目 `pom.xml` 已引入：

- `jjwt-api`
- `jjwt-impl`
- `jjwt-jackson`

版本：`0.11.5`

---

## 方法一览

| 方法 | 说明 |
| --- | --- |
| `createToken(subject, secret, expireMillis)` | 生成 token |
| `createToken(subject, claims, secret, expireMillis)` | 生成带自定义声明的 token |
| `parseClaims(token, secret)` | 解析全部声明 |
| `getSubject(token, secret)` | 获取 subject |
| `getClaim(token, secret, key)` | 获取指定声明 |
| `getClaimAsString(token, secret, key)` | 获取声明并转字符串 |
| `isValid(token, secret)` | 是否有效（签名正确且未过期） |
| `isExpired(token, secret)` | 是否已过期 |
| `getExpiration(token, secret)` | 获取过期时间 |

---

## 使用示例

```java
import com.utility.JwtUtil;
import java.util.HashMap;
import java.util.Map;

String secret = "01234567890123456789012345678901"; // ≥ 32 位

Map<String, Object> claims = new HashMap<>();
claims.put("role", "admin");

// 生成：2 小时有效
String token = JwtUtil.createToken("user1001", claims, secret, 2 * 60 * 60 * 1000L);

// 校验 / 取值
boolean ok = JwtUtil.isValid(token, secret);
String subject = JwtUtil.getSubject(token, secret);       // user1001
String role = JwtUtil.getClaimAsString(token, secret, "role"); // admin
```

---

## 注意事项

- `secret` 少于 32 字节会抛异常
- token 非法、签名错误、已过期时，`parseClaims` 等解析方法抛出 `IllegalArgumentException`
- `isValid` 内部捕获异常并返回 `false`，适合做快速判断
