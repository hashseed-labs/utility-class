# utility-class 工具类文档

基于 **JDK 8** 的常用工具集合。本文档按工具类拆分，便于查阅。

---

## 文档目录

### 基础数据处理

| 工具类 | 说明 | 文档 |
| --- | --- | --- |
| `StringUtil` | 字符串常用处理 | [StringUtil.md](./StringUtil.md) |
| `ObjectUtil` | 对象空安全处理 | [ObjectUtil.md](./ObjectUtil.md) |
| `ArrayUtil` | 数组判空、拼接、去重 | [ArrayUtil.md](./ArrayUtil.md) |
| `MapUtil` | Map 取值、快速构建 | [MapUtil.md](./MapUtil.md) |
| `CollectionUtil` | 集合交并补、按字段匹配、排序 | [CollectionUtil.md](./CollectionUtil.md) |
| `ConvertUtil` | 常用类型转换 | [ConvertUtil.md](./ConvertUtil.md) |
| `CalculateUtil` | 四则运算 | [CalculateUtil.md](./CalculateUtil.md) |
| `CompareUtil` | 数值 / 日期大小比较 | [CompareUtil.md](./CompareUtil.md) |
| `DateUtil` | 时间格式化、解析、同一天判断 | [DateUtil.md](./DateUtil.md) |

### 校验 / 随机 / ID

| 工具类 | 说明 | 文档 |
| --- | --- | --- |
| `AssertUtil` | 参数断言 | [AssertUtil.md](./AssertUtil.md) |
| `RegexUtil` | 正则与常用格式校验 | [RegexUtil.md](./RegexUtil.md) |
| `RandomUtil` | 随机数 / 随机串 | [RandomUtil.md](./RandomUtil.md) |
| `IdUtil` | UUID / 雪花 ID | [IdUtil.md](./IdUtil.md) |

### 加解密 / 摘要 / 令牌

| 工具类 | 说明 | 文档 |
| --- | --- | --- |
| `AesUtil` | AES 对称加解密 | [AesUtil.md](./AesUtil.md) |
| `RsaUtil` | RSA 非对称加解密 | [RsaUtil.md](./RsaUtil.md) |
| `DigestUtil` | MD5 / SHA / HMAC | [DigestUtil.md](./DigestUtil.md) |
| `JwtUtil` | JWT 生成与解析 | [JwtUtil.md](./JwtUtil.md) |

### Web / IO

| 工具类 | 说明 | 文档 |
| --- | --- | --- |
| `RequestParamUtil` | GET / POST / PathVariable 取参 | [RequestParamUtil.md](./RequestParamUtil.md) |
| `IpUtil` | 客户端 IP / 内网判断 | [IpUtil.md](./IpUtil.md) |
| `IoUtil` | 流读写与关闭 | [IoUtil.md](./IoUtil.md) |
| `FileUtil` | 文件读写复制删除 | [FileUtil.md](./FileUtil.md) |

前后端加解密完整对接流程：[加解密工具使用说明.md](./加解密工具使用说明.md)

---

## 包名

```text
com.utility
```

## 依赖说明

| 能力 | 依赖 |
| --- | --- |
| 绝大多数工具类 | 仅需 JDK 8 |
| `JwtUtil` | jjwt 0.11.5 |
| `RequestParamUtil` / `IpUtil` | javax.servlet-api（`provided`） |

---

## 统一约定

- 工具类均为 `final`，构造方法私有，仅提供静态方法
- 非法参数或转换 / 加解密失败时，通常抛出 `IllegalArgumentException`
- 集合 / 数组类运算尽量**不修改入参**，返回新结果

---

## 本次补齐说明

相对原有能力，本次新增：

`ObjectUtil`、`ArrayUtil`、`MapUtil`、`RandomUtil`、`RegexUtil`、`DigestUtil`、`IoUtil`、`FileUtil`、`AssertUtil`、`IpUtil`、`IdUtil`

若后续还需要，可继续扩展：`JsonUtil`（需 Jackson/Gson）、`BeanUtil`（对象拷贝）、`TreeUtil`（树形组装）、`PageUtil`（分页计算）等。
