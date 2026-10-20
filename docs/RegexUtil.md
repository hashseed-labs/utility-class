# RegexUtil

正则校验与提取工具类。

**包名：** `com.utility.RegexUtil`

## 内置正则

| 常量 / 方法 | 说明 |
| --- | --- |
| `isMobile` | 大陆手机号 |
| `isEmail` | 邮箱 |
| `isIdCard` | 18 位身份证格式 |
| `isUrl` | URL |
| `isIpv4` | IPv4 |
| `isInteger` | 整数 |

## 通用方法

| 方法 | 说明 |
| --- | --- |
| `isMatch` | 全匹配 |
| `contains` | 是否包含匹配 |
| `findAll` | 提取全部匹配 |
| `replaceAll` | 正则替换 |

```java
RegexUtil.isMobile("13812345678"); // true
RegexUtil.isEmail("a@b.com");      // true
RegexUtil.findAll("\\d+", "a12b34"); // ["12","34"]
```
