# RsaUtil

RSA 非对称加解密工具类。

**包名：** `com.utility.RsaUtil`

前后端完整对接流程见：[加解密工具使用说明.md](./加解密工具使用说明.md)

---

## 功能简介

常用于加密传输 **AES 密钥**（混合加密），不建议直接加密大段业务 JSON。

| 项 | 约定 |
| --- | --- |
| 算法 | `RSA/ECB/PKCS1Padding` |
| 建议密钥长度 | 2048 位 |
| 输出 | `Base64(密文 )` |

---

## 方法一览

| 方法 | 说明 |
| --- | --- |
| `generateKeyPair()` | 生成 2048 位密钥对，`[0]` 公钥、`[1]` 私钥（Base64） |
| `generateKeyPair(keySize)` | 指定位数生成密钥对 |
| `toPublicKeyPem(publicKeyBase64)` | Base64 公钥 → PEM（便于 JSEncrypt） |
| `toPrivateKeyPem(privateKeyBase64)` | Base64 私钥 → PEM |
| `encryptByPublicKey(...)` | 公钥加密 |
| `decryptByPrivateKey(...)` | 私钥解密 |
| `encryptByPrivateKey(...)` | 私钥加密 |
| `decryptByPublicKey(...)` | 公钥解密 |

---

## 使用示例

```java
import com.utility.RsaUtil;

// 1. 生成密钥对：公钥给前端，私钥仅后端保存
String[] keyPair = RsaUtil.generateKeyPair();
String publicKey = keyPair[0];
String privateKey = keyPair[1];

// 2. 给前端时转成 PEM
String publicKeyPem = RsaUtil.toPublicKeyPem(publicKey);

// 3. 前端用公钥加密 AES 密钥后，后端私钥解密
String aesKey = "0123456789abcdef";
String encryptedKey = RsaUtil.encryptByPublicKey(aesKey, publicKey);
String decryptedKey = RsaUtil.decryptByPrivateKey(encryptedKey, privateKey);
```

### 混合加密中的典型用法

```java
// 后端：解出本次请求的 AES 密钥，再交给 AesUtil 解密业务数据
String aesKey = RsaUtil.decryptByPrivateKey(request.getKey(), RSA_PRIVATE_KEY);
String bodyJson = AesUtil.decrypt(request.getData(), aesKey);
```

---

## 注意事项

- **私钥只放服务端**，不要写入前端或提交到仓库明文
- RSA 适合短数据（如 AES 密钥）；大报文请用 `AesUtil`
- 密钥非法、加解密失败时抛出 `IllegalArgumentException`
