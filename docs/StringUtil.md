# StringUtil

字符串处理工具类，覆盖日常开发中最常用的能力。

**包名：** `com.utility.StringUtil`

---

## 功能简介

| 分类 | 能力 |
| --- | --- |
| 判空 | `isEmpty` / `isBlank` / `isNotEmpty` / `isNotBlank` |
| 默认值 / 裁剪 | `nullToEmpty` / `defaultIfBlank` / `trim` / `trimToNull` |
| 比较 / 包含 | `equals` / `equalsIgnoreCase` / `contains` / `startsWith` / `endsWith` |
| 截取 / 补齐 | `substring` / `left` / `right` / `abbreviate` / `padLeft` / `padRight` |
| 替换 / 移除 | `replace` / `remove` / `removeWhitespace` |
| 大小写 / 命名 | `capitalize` / `underlineToCamel` / `camelToUnderline` |
| 拼接 / 分割 | `join` / `splitTrim` |
| 其他 | `isNumeric` / `uuid` / `hide` 脱敏 / `format` |

---

## 方法一览

### 判空

| 方法 | 说明 |
| --- | --- |
| `isEmpty(text)` | `null` 或长度为 0 |
| `isBlank(text)` | `null`、空串或全空白 |
| `isNotEmpty` / `isNotBlank` | 上述取反 |

### 默认值 / 裁剪

| 方法 | 说明 |
| --- | --- |
| `nullToEmpty` / `emptyToNull` | null ↔ 空串 |
| `defaultIfBlank` / `defaultIfEmpty` | 空白 / 空时给默认值 |
| `trim` / `trimToNull` / `trimToEmpty` | 去首尾空白 |

### 截取 / 变形

| 方法 | 说明 |
| --- | --- |
| `substring(text, start, end)` | 安全截取，自动处理越界 |
| `left` / `right` | 左 / 右截取 |
| `abbreviate(text, maxWidth)` | 超长省略 |
| `padLeft` / `padRight` | 左右补齐 |
| `repeat` / `reverse` | 重复 / 反转 |

### 命名转换

| 方法 | 示例 |
| --- | --- |
| `underlineToCamel` | `user_name` → `userName` |
| `camelToUnderline` | `userName` → `user_name` |
| `capitalize` / `uncapitalize` | 首字母大写 / 小写 |

### 其他常用

| 方法 | 说明 |
| --- | --- |
| `join(delimiter, ...)` | 拼接，自动跳过 `null` |
| `splitTrim(text, sep)` | 分割并去掉空白项 |
| `isNumeric` | 是否全数字 |
| `byteLength` | UTF-8 字节长度 |
| `uuid` / `uuidStandard` | 无横线 / 标准 UUID |
| `hide(text, prefix, suffix)` | 中间脱敏 |
| `format(template, args...)` | `{}` 占位格式化 |

---

## 使用示例

```java
import com.utility.StringUtil;
import java.util.Arrays;

// 判空
StringUtil.isBlank("   ");          // true
StringUtil.isNotBlank("hello");     // true

// 默认值 / 裁剪
StringUtil.defaultIfBlank(null, "-"); // "-"
StringUtil.trimToNull("  ");          // null

// 比较
StringUtil.equals(null, null);        // true
StringUtil.equalsIgnoreCase("Ab", "ab"); // true

// 截取 / 省略
StringUtil.left("hello", 2);          // "he"
StringUtil.abbreviate("你好世界欢迎", 5); // "你好..."

// 命名转换
StringUtil.underlineToCamel("user_name"); // "userName"
StringUtil.camelToUnderline("userName");  // "user_name"

// 拼接 / 分割
StringUtil.join(",", "a", null, "b");     // "a,b"
StringUtil.splitTrim("a, b, ,c", ",");    // ["a","b","c"]

// 脱敏 / 格式化 / UUID
StringUtil.hide("13812345678", 3, 4);     // "138****5678"
StringUtil.format("你好，{}，今天是{}", "张三", "周一");
StringUtil.uuid();                        // 32 位无横线
```

---

## 注意事项

- 多数方法对 `null` 做了空安全处理，不会轻易 NPE
- `format` 使用 `{}` 占位，参数不足时保留原 `{}`
- `hide` 当保留长度之和 ≥ 原串长度时，原样返回
