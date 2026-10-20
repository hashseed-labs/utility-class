# DateUtil

日期时间工具类。

**包名：** `com.utility.DateUtil`

---

## 功能简介

- 时间格式化（`Date` / `LocalDateTime` / `LocalDate` → 字符串）
- 字符串解析为日期
- 判断是否相等、是否同一天
- `Date` 与 `LocalDateTime` / `LocalDate` 互转

---

## 常用格式常量

| 常量 | 值 |
| --- | --- |
| `PATTERN_DATETIME` | `yyyy-MM-dd HH:mm:ss` |
| `PATTERN_DATE` | `yyyy-MM-dd` |
| `PATTERN_TIME` | `HH:mm:ss` |
| `PATTERN_DATETIME_COMPACT` | `yyyyMMddHHmmss` |
| `PATTERN_DATE_COMPACT` | `yyyyMMdd` |

---

## 方法一览

### 格式化

| 方法 | 说明 |
| --- | --- |
| `format(date)` | `Date` → 默认日期时间格式 |
| `format(date, pattern)` | `Date` → 指定格式 |
| `format(localDateTime[, pattern])` | `LocalDateTime` 格式化 |
| `format(localDate[, pattern])` | `LocalDate` 格式化（默认 `yyyy-MM-dd`） |

### 解析

| 方法 | 说明 |
| --- | --- |
| `parseToDate(text[, pattern])` | 字符串 → `Date` |
| `parseToLocalDateTime(text[, pattern])` | 字符串 → `LocalDateTime` |
| `parseToLocalDate(text[, pattern])` | 字符串 → `LocalDate` |

### 比较

| 方法 | 说明 |
| --- | --- |
| `isEqual(...)` | 是否完全相等（`Date` 精确到毫秒） |
| `isSameDay(...)` | 是否同一天（忽略时分秒） |

### 互转

| 方法 | 说明 |
| --- | --- |
| `toLocalDateTime(date)` | `Date` → `LocalDateTime` |
| `toDate(localDateTime)` | `LocalDateTime` → `Date` |
| `toDate(localDate)` | `LocalDate` → 当天 00:00:00 的 `Date` |

---

## 使用示例

```java
import com.utility.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

// 格式化
DateUtil.format(new Date());
// "2026-10-20 14:23:00"

DateUtil.format(LocalDate.now(), "yyyy年MM月dd日");

// 解析
Date date = DateUtil.parseToDate("2026-10-20 14:23:00");
LocalDateTime ldt = DateUtil.parseToLocalDateTime("2026-10-20 14:23:00");
LocalDate ld = DateUtil.parseToLocalDate("2026-10-20");

// 相等 / 同一天
DateUtil.isEqual(d1, d2);    // 时分秒也要相同
DateUtil.isSameDay(d1, d2);  // 只要年月日相同
```

---

## 注意事项

- 解析失败或格式不匹配时抛出异常
- 解析 `Date` 时关闭宽松模式，非法日期不会被自动纠错
- 空字符串解析返回 `null`；`date == null` 时 `format` 返回 `null`
- `isEqual` / `isSameDay`：两者都为 `null` 视为相等 / 同一天
