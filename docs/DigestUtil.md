# DigestUtil

摘要 / 签名工具类（MD5、SHA、HMAC）。

**包名：** `com.utility.DigestUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `md5(text)` | MD5 十六进制 |
| `sha1(text)` | SHA-1 |
| `sha256(text)` | SHA-256 |
| `hmacSha256(text, secret)` | HmacSHA256 |
| `toHex(bytes)` | 字节转十六进制 |

```java
DigestUtil.md5("hello");
DigestUtil.sha256("hello");
DigestUtil.hmacSha256("payload", "secret");
```

> 返回值均为小写十六进制字符串。
