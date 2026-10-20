# CompareUtil

数值与日期比较工具类。

**包名：** `com.utility.CompareUtil`

---

## 功能简介

- 比较两个**数值**的大小
- 比较两个**日期**的大小（`Date` / `LocalDateTime` / `LocalDate`）

API 风格统一：`compare` / `gt` / `ge` / `lt` / `le` / `eq` / `max` / `min`

---

## 方法一览

### 数值

| 方法 | 说明 |
| --- | --- |
| `compare(num1, num2)` | `<` 返回负数，`=` 返回 `0`，`>` 返回正数 |
| `gt` / `ge` / `lt` / `le` / `eq` | 大于 / 大于等于 / 小于 / 小于等于 / 相等 |
| `max` / `min` | 返回较大 / 较小值（`BigDecimal`） |

### 日期（`Date` / `LocalDateTime` / `LocalDate` 均有对应重载）

| 方法 | 说明 |
| --- | --- |
| `compare(d1, d2)` | 前者早返回负数，相等 `0`，晚返回正数 |
| `gt` / `ge` / `lt` / `le` / `eq` | 晚于 / 不早于 / 早于 / 不晚于 / 相等 |
| `max` / `min` | 返回较晚 / 较早的日期 |

---

## 使用示例

```java
import com.utility.CompareUtil;
import java.time.LocalDate;
import java.util.Date;

// 数值
CompareUtil.gt(10, 3);          // true
CompareUtil.eq(1.0, 1.00);      // true（按数值比较）
CompareUtil.max(10, 3);         // 10

// 日期
Date d1 = ...;
Date d2 = ...;
CompareUtil.lt(d1, d2);         // d1 是否早于 d2
CompareUtil.max(d1, d2);        // 较晚的那个

LocalDate today = LocalDate.now();
LocalDate end = LocalDate.of(2026, 12, 31);
CompareUtil.le(today, end);     // true
```

---

## 注意事项

- 任一比较参数为 `null` 时抛出异常
- 数值比较基于 `BigDecimal`，`1.0` 与 `1.00` 视为相等
- `Date` 比较精确到毫秒
