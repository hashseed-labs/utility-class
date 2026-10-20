# AesUtil

AES 对称加解密工具类。

**包名：** `com.utility.AesUtil`

前后端完整对接流程见：[加解密工具使用说明.md](./加解密工具使用说明.md)

---

## 功能简介

用于加密请求体、响应体等业务数据。

| 项 | 约定 |
| --- | --- |
| 算法 | `AES/CBC/PKCS5Padding` |
| 密钥长度 | 16 / 24 / 32 字节（AES-128 / 192 / 256） |
| IV | 每次加密随机 16 字节 |
| 输出 | `Base64( IV[16] + 密文 )` |

---

## 方法一览

| 方法 | 说明 |
| --- | --- |
| `encrypt(plainText, key)` | 字符串加密 → Base64 密文 |
| `decrypt(cipherText, key)` | Base64 密文解密 → 字符串 |
| `encrypt(plainBytes, keyBytes)` | 字节加密 → `IV + 密文` |
| `decrypt(cipherBytes, keyBytes)` | 字节解密 |
| `generateKey()` | 生成 128 位随机密钥（Base64） |
| `generateKey(bitLength)` | 生成 128 / 192 / 256 位密钥（Base64） |

---

## 使用示例

```java
import com.utility.AesUtil;

// 随机密钥（Base64）
String key = AesUtil.generateKey(128);

// 或固定 16 字节字符串密钥
// String key = "0123456789abcdef";

String plain = "{\"userId\":1001,\"amount\":99.5}";
String cipher = AesUtil.encrypt(plain, key);
String back = AesUtil.decrypt(cipher, key);
```

### 后端响应加密示例

```java
String responseJson = "{\"code\":0,\"msg\":\"ok\"}";
String encrypted = AesUtil.encrypt(responseJson, aesKey);
// 将 encrypted 作为响应 data 返回前端
```

### 后端请求解密示例

```java
String requestJson = AesUtil.decrypt(encryptedBody, aesKey);
// 再反序列化为业务对象
```

---

## 注意事项

- 密钥也支持 Base64 字符串（由 `generateKey` 生成）；解码后长度须为 16 / 24 / 32
- 明文、密文、密钥为空或长度非法时抛出异常
- 与前端 CryptoJS 对接时，密文格式必须同为 `Base64(IV + ciphertext)`
