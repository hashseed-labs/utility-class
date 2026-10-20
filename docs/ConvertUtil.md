# ConvertUtil

类型转换工具类。

**包名：** `com.utility.ConvertUtil`

---

## 功能简介

支持常用类型之间的转换。源值为空或转换失败时，抛出 `IllegalArgumentException`。

---

## 方法一览

| 目标类型 | 方法 | 说明 |
| --- | --- | --- |
| `String` | `toStr` | 任意对象转字符串 |
| `Integer` / `int` | `toInt` / `toIntValue` | 转整数 |
| `Long` / `long` | `toLong` / `toLongValue` | 转长整数；`Date` 可转毫秒时间戳 |
| `Double` / `double` | `toDouble` / `toDoubleValue` | 转双精度 |
| `Float` / `float` | `toFloat` / `toFloatValue` | 转单精度 |
| `Boolean` / `boolean` | `toBool` / `toBoolValue` | 转布尔 |
| `BigDecimal` | `toBigDecimal` | 转高精度小数 |
| `Date` | `toDate` | 转日期 |

### 布尔值支持的字符串

`true` / `false`、`1` / `0`、`yes` / `no`、`y` / `n`、`是` / `否`（忽略大小写）

### 日期支持的输入

- 已是 `Date`
- `Number`（按毫秒时间戳）
- 字符串格式：`yyyy-MM-dd HH:mm:ss`、`yyyy-MM-dd`、`yyyyMMddHHmmss`、`yyyyMMdd`
- 10 / 13 位纯数字：按秒 / 毫秒时间戳

---

## 使用示例

```java
import com.utility.ConvertUtil;

ConvertUtil.toInt("12");                    // 12
ConvertUtil.toLongValue("100");             // 100L
ConvertUtil.toBool("是");                   // true
ConvertUtil.toBigDecimal("3.14");
ConvertUtil.toDate("2026-10-20 13:00:00");
ConvertUtil.toStr(123);                     // "123"

// 失败会抛异常
// ConvertUtil.toInt("abc");
// ConvertUtil.toBool(null);
```

---

## 注意事项

- 转换失败信息形如：`类型转换失败, value=abc, targetType=Integer`
- 空白字符串视为无效输入
- 与「带默认值、失败返回默认」的风格不同，本工具类采用**失败即抛异常**
